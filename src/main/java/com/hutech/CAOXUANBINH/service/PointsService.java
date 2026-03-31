package com.hutech.CAOXUANBINH.service;

import com.hutech.CAOXUANBINH.model.PointVoucher;
import com.hutech.CAOXUANBINH.repository.OrderRepository;
import com.hutech.CAOXUANBINH.repository.PointVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PointsService {
    public static final int VOUCHER_20K = 20000;
    public static final int VOUCHER_30K = 30000;
    public static final int VOUCHER_35K = 35000;

    private final OrderRepository orderRepository;
    private final PointVoucherRepository pointVoucherRepository;

    public int getAvailablePointsByPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return 0;
        }
        int fromOrders = orderRepository.getAvailablePointsAnyByPhone(phoneNumber);
        int redeemed = pointVoucherRepository.sumRedeemedPointsByPhoneNumber(phoneNumber);
        return Math.max(0, fromOrders - redeemed);
    }

    public int getAvailablePointsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return 0;
        }
        int fromOrders = orderRepository.getAvailablePointsByUsername(username);
        int redeemed = pointVoucherRepository.sumRedeemedPointsByUsername(username);
        return Math.max(0, fromOrders - redeemed);
    }

    public List<PointVoucher> getVouchers(String phoneNumber, String username) {
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            return pointVoucherRepository.findByPhoneNumberOrderByIdDesc(phoneNumber);
        }
        if (username != null && !username.isBlank()) {
            return pointVoucherRepository.findByUsernameOrderByIdDesc(username);
        }
        return Collections.emptyList();
    }

    public Optional<PointVoucher> redeemVoucher(String phoneNumber, String username, int discountAmount) {
        Integer requiredPoints = getRequiredPoints(discountAmount);
        if (requiredPoints == null) {
            return Optional.empty();
        }

        int availablePoints = (phoneNumber != null && !phoneNumber.isBlank())
                ? getAvailablePointsByPhone(phoneNumber)
                : getAvailablePointsByUsername(username);
        if (availablePoints < requiredPoints) {
            return Optional.empty();
        }

        PointVoucher voucher = new PointVoucher();
        voucher.setCode("PV" + discountAmount + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        voucher.setPhoneNumber(phoneNumber);
        voucher.setUsername(username);
        voucher.setRedeemedPoints(requiredPoints);
        voucher.setDiscountAmount(discountAmount);
        voucher.setUsed(false);
        voucher.setCreatedAt(LocalDateTime.now());
        return Optional.of(pointVoucherRepository.save(voucher));
    }

    public Integer getRequiredPoints(int discountAmount) {
        if (discountAmount == VOUCHER_20K) {
            return 1000;
        }
        if (discountAmount == VOUCHER_30K) {
            return 2000;
        }
        if (discountAmount == VOUCHER_35K) {
            return 3000;
        }
        return null;
    }

    public Optional<PointVoucher> validateVoucher(String code, String phoneNumber, String username) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        Optional<PointVoucher> voucherOpt = pointVoucherRepository.findByCode(code.trim().toUpperCase());
        if (voucherOpt.isEmpty()) {
            return Optional.empty();
        }
        PointVoucher voucher = voucherOpt.get();
        if (voucher.isUsed()) {
            return Optional.empty();
        }

        boolean isOwnerByUser = username != null && !username.isBlank() && username.equals(voucher.getUsername());
        boolean isOwnerByPhone = phoneNumber != null && !phoneNumber.isBlank()
                && phoneNumber.equals(voucher.getPhoneNumber());
        if (!isOwnerByUser && !isOwnerByPhone) {
            return Optional.empty();
        }
        return Optional.of(voucher);
    }

    public void markVoucherUsed(PointVoucher voucher) {
        voucher.setUsed(true);
        voucher.setUsedAt(LocalDateTime.now());
        pointVoucherRepository.save(voucher);
    }
}
