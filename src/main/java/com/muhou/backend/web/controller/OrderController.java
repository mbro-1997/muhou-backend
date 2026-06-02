package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.OrderApplicationService;
import com.muhou.backend.application.service.AdminApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.CreateOrderRequest;
import com.muhou.backend.web.request.FactoryRejectOrderRequest;
import com.muhou.backend.web.request.OrderDisputeRequest;
import com.muhou.backend.web.request.OrderPayRequest;
import com.muhou.backend.web.request.OrderReviewRequest;
import com.muhou.backend.web.request.ScanOrderItemRequest;
import com.muhou.backend.web.response.OrderResponse;
import com.muhou.backend.web.response.PayResponse;
import com.muhou.backend.web.response.DisputeResponse;
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
@RequestMapping("/api")
public class OrderController {

    private final OrderApplicationService orderApplicationService;
    private final AdminApplicationService adminApplicationService;

    public OrderController(OrderApplicationService orderApplicationService,
                           AdminApplicationService adminApplicationService) {
        this.orderApplicationService = orderApplicationService;
        this.adminApplicationService = adminApplicationService;
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> list(@RequestParam(required = false) String role) {
        return ApiResponse.success(orderApplicationService.listOrders(role));
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderResponse> detail(@PathVariable Long orderId) {
        return ApiResponse.success(orderApplicationService.getOrder(orderId));
    }

    @PostMapping("/orders/checkout")
    public ApiResponse<PayResponse> checkout(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(orderApplicationService.checkoutProjectOrders(request));
    }

    @PostMapping("/orders/{orderId}/pay")
    public ApiResponse<PayResponse> pay(@PathVariable Long orderId, @RequestBody(required = false) OrderPayRequest request) {
        return ApiResponse.success(orderApplicationService.payOrder(orderId, request == null ? new OrderPayRequest() : request));
    }

    @PostMapping("/factory/orders/{orderId}/confirm")
    public ApiResponse<OrderResponse> confirm(@PathVariable Long orderId) {
        return ApiResponse.success(orderApplicationService.confirmOrderByFactory(orderId));
    }

    @PostMapping("/factory/orders/{orderId}/reject")
    public ApiResponse<OrderResponse> reject(@PathVariable Long orderId, @Valid @RequestBody FactoryRejectOrderRequest request) {
        return ApiResponse.success(orderApplicationService.rejectOrderByFactory(orderId, request.getRejectReason()));
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelByDemander(@PathVariable Long orderId) {
        return ApiResponse.success(orderApplicationService.cancelOrderByDemander(orderId));
    }

    @PostMapping("/factory/orders/{orderId}/cancel-after-confirm")
    public ApiResponse<OrderResponse> cancelAfterConfirmByFactory(@PathVariable Long orderId) {
        return ApiResponse.success(orderApplicationService.cancelAfterConfirmByFactory(orderId));
    }

    @PostMapping("/factory/orders/{orderId}/outbound-scan/{propId}")
    public ApiResponse<OrderResponse> outbound(@PathVariable Long orderId, @PathVariable Long propId) {
        return ApiResponse.success(orderApplicationService.scanOutbound(orderId, propId));
    }

    @PostMapping("/factory/orders/{orderId}/outbound-scan")
    public ApiResponse<OrderResponse> outboundByBody(@PathVariable Long orderId, @Valid @RequestBody ScanOrderItemRequest request) {
        if (request.getQrCodeId() != null && !request.getQrCodeId().isBlank()) {
            return ApiResponse.success(orderApplicationService.scanOutboundByQrCode(orderId, request.getQrCodeId()));
        }
        return ApiResponse.success(orderApplicationService.scanOutbound(orderId, request.getPropId()));
    }

    @PostMapping("/factory/orders/{orderId}/return-scan/{propId}")
    public ApiResponse<OrderResponse> returnScan(@PathVariable Long orderId, @PathVariable Long propId) {
        return ApiResponse.success(orderApplicationService.scanReturn(orderId, propId));
    }

    @PostMapping("/factory/orders/{orderId}/return-scan")
    public ApiResponse<OrderResponse> returnScanByBody(@PathVariable Long orderId, @Valid @RequestBody ScanOrderItemRequest request) {
        if (request.getQrCodeId() != null && !request.getQrCodeId().isBlank()) {
            return ApiResponse.success(orderApplicationService.scanReturnByQrCode(orderId, request.getQrCodeId()));
        }
        return ApiResponse.success(orderApplicationService.scanReturn(orderId, request.getPropId()));
    }

    @PostMapping("/orders/{orderId}/reviews")
    public ApiResponse<OrderResponse> review(@PathVariable Long orderId, @Valid @RequestBody OrderReviewRequest request) {
        return ApiResponse.success(orderApplicationService.reviewOrder(orderId, request));
    }

    @PostMapping("/orders/{orderId}/disputes")
    public ApiResponse<OrderResponse> applyDispute(@PathVariable Long orderId, @Valid @RequestBody OrderDisputeRequest request) {
        return ApiResponse.success(orderApplicationService.applyDispute(orderId, request));
    }

    @GetMapping("/orders/disputes")
    public ApiResponse<List<DisputeResponse>> myDisputes() {
        return ApiResponse.success(adminApplicationService.listMyDisputes());
    }
}
