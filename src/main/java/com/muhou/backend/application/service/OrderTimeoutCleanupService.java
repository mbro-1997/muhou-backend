package com.muhou.backend.application.service;

import com.muhou.backend.common.config.properties.MuhouAppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderTimeoutCleanupService {

    private static final Logger log = LoggerFactory.getLogger(OrderTimeoutCleanupService.class);

    private final OrderApplicationService orderApplicationService;
    private final MuhouAppProperties properties;

    public OrderTimeoutCleanupService(OrderApplicationService orderApplicationService,
                                      MuhouAppProperties properties) {
        this.orderApplicationService = orderApplicationService;
        this.properties = properties;
    }

    @Scheduled(fixedDelayString = "${muhou.order.timeout-cleanup-delay-ms:60000}")
    public void cleanupTimeoutOrders() {
        if (!properties.getOrder().isTimeoutAutoCancelEnabled()) {
            return;
        }
        int affected = orderApplicationService.cancelTimeoutOrders();
        if (affected > 0) {
            log.info("Cancelled {} timeout orders and released locked props", affected);
        }
    }

    @Scheduled(fixedDelayString = "${muhou.order.auto-good-review-delay-ms:3600000}")
    public void cleanupExpiredWaitReviewOrders() {
        if (!properties.getOrder().isAutoGoodReviewEnabled()) {
            return;
        }
        int autoReviewed = orderApplicationService.autoGoodReviewExpiredOrders();
        if (autoReviewed > 0) {
            log.info("Created {} default good reviews for expired wait-review orders", autoReviewed);
        }
    }
}
