package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.PropApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.PropInstanceStatusUpdateRequest;
import com.muhou.backend.web.response.PropInstanceResponse;
import com.muhou.backend.web.response.PropInstanceStatusLogResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/factory")
public class FactoryPropInstanceController {

    private final PropApplicationService propApplicationService;

    public FactoryPropInstanceController(PropApplicationService propApplicationService) {
        this.propApplicationService = propApplicationService;
    }

    @GetMapping("/props/{propId}/instances")
    public ApiResponse<List<PropInstanceResponse>> propInstances(@PathVariable Long propId) {
        return ApiResponse.success(propApplicationService.listFactoryPropInstances(propId));
    }

    @PostMapping("/prop-instances/{instanceId}/status")
    public ApiResponse<PropInstanceResponse> updateStatus(@PathVariable Long instanceId,
                                                          @Valid @RequestBody PropInstanceStatusUpdateRequest request) {
        return ApiResponse.success(propApplicationService.updateFactoryInstanceStatus(instanceId, request.getTargetStatus(), request.getReason()));
    }

    @GetMapping("/prop-instances/{instanceId}/status-logs")
    public ApiResponse<List<PropInstanceStatusLogResponse>> statusLogs(@PathVariable Long instanceId) {
        return ApiResponse.success(propApplicationService.listFactoryInstanceStatusLogs(instanceId));
    }
}
