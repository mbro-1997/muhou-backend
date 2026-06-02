package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.AdminConsoleApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispute-reasons")
public class DisputeReasonController {

    private final AdminConsoleApplicationService adminConsoleApplicationService;

    public DisputeReasonController(AdminConsoleApplicationService adminConsoleApplicationService) {
        this.adminConsoleApplicationService = adminConsoleApplicationService;
    }

    @GetMapping
    public ApiResponse<?> list(@RequestParam String stage, @RequestParam String role) {
        return ApiResponse.success(adminConsoleApplicationService.listDisputeReasons(stage, role));
    }
}
