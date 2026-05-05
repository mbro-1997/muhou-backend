package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.UserApplicationService;
import com.muhou.backend.application.service.FactoryOnboardingApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.FactoryApplicationSubmitRequest;
import com.muhou.backend.web.request.FactoryInviteVerifyRequest;
import com.muhou.backend.web.request.UserProfileUpdateRequest;
import com.muhou.backend.web.request.WechatPhoneBindRequest;
import com.muhou.backend.web.response.FactoryAuditStatusResponse;
import com.muhou.backend.web.response.FactoryInviteVerifyResponse;
import com.muhou.backend.web.response.UserProfileResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userApplicationService;
    private final FactoryOnboardingApplicationService factoryOnboardingApplicationService;

    public UserController(UserApplicationService userApplicationService,
                          FactoryOnboardingApplicationService factoryOnboardingApplicationService) {
        this.userApplicationService = userApplicationService;
        this.factoryOnboardingApplicationService = factoryOnboardingApplicationService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me(@RequestParam(required = false) String role) {
        return ApiResponse.success(userApplicationService.getCurrentUser(role));
    }

    @PostMapping("/bind-demander")
    public ApiResponse<UserProfileResponse> bindDemander() {
        factoryOnboardingApplicationService.bindDemanderForCurrentUser();
        return ApiResponse.success(userApplicationService.getCurrentUser(null));
    }

    @PutMapping("/profile")
    public ApiResponse<UserProfileResponse> updateProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        return ApiResponse.success(userApplicationService.updateCurrentUserProfile(request));
    }

    @PostMapping("/phone/wechat")
    public ApiResponse<UserProfileResponse> bindWechatPhone(@Valid @RequestBody WechatPhoneBindRequest request) {
        return ApiResponse.success(userApplicationService.bindCurrentUserWechatPhone(request.getCode()));
    }

    @PostMapping("/factory-invite/verify")
    public ApiResponse<FactoryInviteVerifyResponse> verifyFactoryInvite(@Valid @RequestBody FactoryInviteVerifyRequest request) {
        return ApiResponse.success(factoryOnboardingApplicationService.verifyInviteCodeForCurrentUser(request.getCode()));
    }

    @PostMapping("/factory-applications")
    public ApiResponse<FactoryAuditStatusResponse> submitFactoryApplication(@Valid @RequestBody FactoryApplicationSubmitRequest request) {
        return ApiResponse.success(factoryOnboardingApplicationService.submitFactoryApplicationForCurrentUser(request));
    }

    @GetMapping("/factory-audit-status")
    public ApiResponse<FactoryAuditStatusResponse> factoryAuditStatus() {
        return ApiResponse.success(factoryOnboardingApplicationService.getCurrentFactoryAuditStatus());
    }
}
