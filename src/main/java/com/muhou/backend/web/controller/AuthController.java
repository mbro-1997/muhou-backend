package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.AuthApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.AdminLoginRequest;
import com.muhou.backend.web.request.AuthLoginRequest;
import com.muhou.backend.web.request.DemoLoginRequest;
import com.muhou.backend.web.response.AuthLoginResponse;
import com.muhou.backend.web.response.DemoAccountResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/wechat-login")
    public ApiResponse<AuthLoginResponse> wechatLogin(@Valid @RequestBody AuthLoginRequest request) {
        return ApiResponse.success(authApplicationService.login(request.getCode(), request.getDesiredRole(), Boolean.TRUE.equals(request.getRealWechat())));
    }

    @GetMapping("/demo-accounts")
    public ApiResponse<List<DemoAccountResponse>> listDemoAccounts() {
        return ApiResponse.success(authApplicationService.listDemoAccounts());
    }

    @PostMapping("/demo-login")
    public ApiResponse<AuthLoginResponse> demoLogin(@Valid @RequestBody DemoLoginRequest request) {
        return ApiResponse.success(authApplicationService.demoLogin(request.getAccountKey(), request.getRole(), Boolean.TRUE.equals(request.getResetDemo())));
    }

    @PostMapping("/admin-login")
    public ApiResponse<AuthLoginResponse> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.success(authApplicationService.adminLogin(request.getUsername(), request.getPassword()));
    }
}
