package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.PropApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.response.QrCodeResolveResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/qrcodes")
public class QrCodeController {

    private final PropApplicationService propApplicationService;

    public QrCodeController(PropApplicationService propApplicationService) {
        this.propApplicationService = propApplicationService;
    }

    @GetMapping("/{qrCodeId}/resolve")
    public ApiResponse<QrCodeResolveResponse> resolve(@PathVariable String qrCodeId) {
        return ApiResponse.success(propApplicationService.resolveQrCode(qrCodeId));
    }

    @GetMapping("/{qrCodeId}/image")
    public ResponseEntity<Resource> image(@PathVariable String qrCodeId) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
            .body(propApplicationService.getQrCodeImage(qrCodeId));
    }
}
