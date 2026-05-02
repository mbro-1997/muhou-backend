package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.PropApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.CreatePropRequest;
import com.muhou.backend.web.request.PropStatusUpdateRequest;
import com.muhou.backend.web.response.PropResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/props")
public class PropController {

    private final PropApplicationService propApplicationService;

    public PropController(PropApplicationService propApplicationService) {
        this.propApplicationService = propApplicationService;
    }

    @GetMapping
    public ApiResponse<List<PropResponse>> list(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) String style,
                                                @RequestParam(required = false) String type,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) String sortBy,
                                                @RequestParam(defaultValue = "desc") String sortOrder) {
        return ApiResponse.success(propApplicationService.listProps(keyword, style, type, status, sortBy, sortOrder));
    }

    @GetMapping("/warehouse/list")
    public ApiResponse<List<PropResponse>> warehouseList() {
        return ApiResponse.success(propApplicationService.listWarehouseProps());
    }

    @GetMapping("/{propId}/warehouse-detail")
    public ApiResponse<PropResponse> warehouseDetail(@PathVariable Long propId) {
        return ApiResponse.success(propApplicationService.getWarehouseProp(propId));
    }

    @GetMapping("/{propId}")
    public ApiResponse<PropResponse> detail(@PathVariable Long propId) {
        return ApiResponse.success(propApplicationService.getPublicProp(propId));
    }

    @GetMapping("/pending-fill/list")
    public ApiResponse<List<PropResponse>> pendingFillList() {
        return ApiResponse.success(propApplicationService.listPendingFillProps());
    }

    @PostMapping
    public ApiResponse<PropResponse> create(@Valid @RequestBody CreatePropRequest request) {
        return ApiResponse.success(propApplicationService.createProp(request));
    }

    @PostMapping("/{propId}/complete-fill")
    public ApiResponse<PropResponse> completeFill(@PathVariable Long propId, @Valid @RequestBody CreatePropRequest request) {
        return ApiResponse.success(propApplicationService.completePendingFill(propId, request));
    }

    @PostMapping("/{propId}/status")
    public ApiResponse<PropResponse> updateStatus(@PathVariable Long propId, @Valid @RequestBody PropStatusUpdateRequest request) {
        return ApiResponse.success(propApplicationService.updatePropStatus(propId, request.getTargetStatus()));
    }
}
