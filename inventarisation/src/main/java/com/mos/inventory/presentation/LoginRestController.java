package com.mos.inventory.presentation;

import com.mos.inventory.service.auth.LoginResult;
import com.mos.inventory.service.auth.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginRestController {

    private LoginService loginService;

    @Autowired
    public LoginRestController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping("/login")
    public LoginResult login(@RequestParam("email") String email) {
        return loginService.login(email);
    }


}
