package com.duchuy.accountservice.controller;

import com.duchuy.accountservice.model.LoginDTO;
import com.duchuy.accountservice.model.LoginResponse;
import com.duchuy.accountservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {
    @Autowired
    UserService loginService;

    @PostMapping
    public Mono<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        return loginService.login(loginDTO);
    }

}
