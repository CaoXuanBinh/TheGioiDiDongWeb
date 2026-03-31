package com.hutech.CAOXUANBINH.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/403")
    public String forbidden() {
        return "403";
    }
}
