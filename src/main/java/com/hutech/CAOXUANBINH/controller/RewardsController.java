package com.hutech.CAOXUANBINH.controller;

import com.hutech.CAOXUANBINH.model.PointVoucher;
import com.hutech.CAOXUANBINH.model.User;
import com.hutech.CAOXUANBINH.service.PointsService;
import com.hutech.CAOXUANBINH.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardsController {
    private static final String OTP_CODE_KEY = "reward_otp_code";
    private static final String OTP_PHONE_KEY = "reward_otp_phone";
    private static final String OTP_AMOUNT_KEY = "reward_otp_amount";
    private static final String OTP_EXPIRE_KEY = "reward_otp_expire";

    private final PointsService pointsService;
    private final UserService userService;

    @GetMapping
    public String rewardsPage(@RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "otpPending", required = false, defaultValue = "false") boolean otpPending,
            Principal principal,
            HttpSession session,
            Model model) {
        String username = principal != null ? principal.getName() : null;
        if ((phone == null || phone.isBlank()) && username != null) {
            User user = userService.findByUsername(username);
            if (user != null && user.getPhone() != null && !user.getPhone().isBlank()) {
                phone = user.getPhone();
            }
        }

        int points = resolvePoints(phone, username);
        List<PointVoucher> vouchers = pointsService.getVouchers(phone, username);
        model.addAttribute("phone", phone);
        model.addAttribute("points", points);
        model.addAttribute("vouchers", vouchers);
        model.addAttribute("otpPending", otpPending);
        model.addAttribute("otpPhone", session.getAttribute(OTP_PHONE_KEY));
        model.addAttribute("otpAmount", session.getAttribute(OTP_AMOUNT_KEY));
        return "rewards/points";
    }

    @GetMapping("/points")
    @ResponseBody
    public int getPoints(@RequestParam("phone") String phone, Principal principal) {
        String username = principal != null ? principal.getName() : null;
        return resolvePoints(phone, username);
    }

    @PostMapping("/request-otp")
    public String requestOtp(@RequestParam("phone") String phone,
            @RequestParam("amount") int amount,
            Principal principal,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String username = principal != null ? principal.getName() : null;
        Integer requiredPoints = pointsService.getRequiredPoints(amount);
        if (requiredPoints == null) {
            redirectAttributes.addFlashAttribute("error", "Muc voucher khong hop le.");
            return "redirect:/rewards?phone=" + phone;
        }

        int points = resolvePoints(phone, username);
        if (points < requiredPoints) {
            redirectAttributes.addFlashAttribute("error",
                    "Khong du diem de doi voucher nay (can " + requiredPoints + " diem).");
            return "redirect:/rewards?phone=" + phone;
        }

        String otpCode = String.format("%06d", new Random().nextInt(1_000_000));
        long expireAt = System.currentTimeMillis() + (5 * 60 * 1000L);

        session.setAttribute(OTP_CODE_KEY, otpCode);
        session.setAttribute(OTP_PHONE_KEY, phone);
        session.setAttribute(OTP_AMOUNT_KEY, amount);
        session.setAttribute(OTP_EXPIRE_KEY, expireAt);

        redirectAttributes.addFlashAttribute("message",
                "OTP da duoc gui. (Demo OTP: " + otpCode + ", hieu luc 5 phut)");
        return "redirect:/rewards?phone=" + phone + "&otpPending=true";
    }

    @PostMapping("/redeem")
    public String redeemWithOtp(@RequestParam("phone") String phone,
            @RequestParam("amount") int amount,
            @RequestParam("otpCode") String otpCode,
            Principal principal,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        String sessionOtp = (String) session.getAttribute(OTP_CODE_KEY);
        String sessionPhone = (String) session.getAttribute(OTP_PHONE_KEY);
        Integer sessionAmount = (Integer) session.getAttribute(OTP_AMOUNT_KEY);
        Long sessionExpire = (Long) session.getAttribute(OTP_EXPIRE_KEY);

        if (sessionOtp == null || sessionPhone == null || sessionAmount == null || sessionExpire == null) {
            redirectAttributes.addFlashAttribute("error", "Ban chua yeu cau OTP.");
            return "redirect:/rewards?phone=" + phone;
        }

        if (System.currentTimeMillis() > sessionExpire) {
            clearOtpSession(session);
            redirectAttributes.addFlashAttribute("error", "OTP da het han. Vui long yeu cau lai.");
            return "redirect:/rewards?phone=" + phone;
        }

        if (!sessionPhone.equals(phone) || sessionAmount != amount || !sessionOtp.equals(otpCode)) {
            redirectAttributes.addFlashAttribute("error", "OTP khong dung.");
            return "redirect:/rewards?phone=" + phone + "&otpPending=true";
        }

        String username = principal != null ? principal.getName() : null;
        Optional<PointVoucher> voucher = pointsService.redeemVoucher(phone, username, amount);
        if (voucher.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Khong the doi voucher. Vui long thu lai.");
            return "redirect:/rewards?phone=" + phone;
        }

        clearOtpSession(session);
        redirectAttributes.addFlashAttribute("message",
                "Doi voucher thanh cong! Ma cua ban: " + voucher.get().getCode());
        return "redirect:/rewards?phone=" + phone;
    }

    private int resolvePoints(String phone, String username) {
        if (phone != null && !phone.isBlank()) {
            return pointsService.getAvailablePointsByPhone(phone);
        }
        if (username != null && !username.isBlank()) {
            return pointsService.getAvailablePointsByUsername(username);
        }
        return pointsService.getAvailablePointsByPhone(phone);
    }

    private void clearOtpSession(HttpSession session) {
        session.removeAttribute(OTP_CODE_KEY);
        session.removeAttribute(OTP_PHONE_KEY);
        session.removeAttribute(OTP_AMOUNT_KEY);
        session.removeAttribute(OTP_EXPIRE_KEY);
    }
}
