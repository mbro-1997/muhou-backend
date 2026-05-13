package com.muhou.backend.application.service;

import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.infrastructure.persistence.entity.OrderScanLogEntity;
import com.muhou.backend.infrastructure.persistence.mapper.OrderScanLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderScanLogApplicationService {

    private final OrderScanLogMapper orderScanLogMapper;
    private final CurrentUserSupport currentUserSupport;

    public OrderScanLogApplicationService(OrderScanLogMapper orderScanLogMapper,
                                          CurrentUserSupport currentUserSupport) {
        this.orderScanLogMapper = orderScanLogMapper;
        this.currentUserSupport = currentUserSupport;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logSuccess(Long orderId,
                           Long orderItemId,
                           Long propId,
                           String qrCodeId,
                           String scanType,
                           Long supplierUserId,
                           String rawScanResult) {
        insert(orderId, orderItemId, propId, qrCodeId, scanType, "success", supplierUserId, rawScanResult, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailure(Long orderId,
                           Long propId,
                           String qrCodeId,
                           String scanType,
                           Long supplierUserId,
                           String rawScanResult,
                           String failReason) {
        insert(orderId, null, propId, qrCodeId, scanType, "failed", supplierUserId, rawScanResult, truncate(failReason, 255));
    }

    private void insert(Long orderId,
                        Long orderItemId,
                        Long propId,
                        String qrCodeId,
                        String scanType,
                        String scanStatus,
                        Long supplierUserId,
                        String rawScanResult,
                        String failReason) {
        OrderScanLogEntity log = new OrderScanLogEntity();
        log.setOrderId(orderId);
        log.setOrderItemId(orderItemId);
        log.setPropId(propId);
        log.setQrCodeId(qrCodeId);
        log.setScanType(scanType);
        log.setScanStatus(scanStatus);
        log.setScanUserId(resolveCurrentUserId());
        log.setSupplierUserId(supplierUserId);
        log.setRawScanResult(truncate(rawScanResult, 1000));
        log.setFailReason(failReason);
        orderScanLogMapper.insert(log);
    }

    private Long resolveCurrentUserId() {
        try {
            return currentUserSupport.requireCurrentUserId();
        } catch (RuntimeException ex) {
            return 0L;
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
