package com.muhou.backend.infrastructure.client;

import com.muhou.backend.common.config.properties.MuhouAppProperties;
import com.muhou.backend.infrastructure.persistence.entity.OrderPaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class MockWechatPaymentGateway implements WechatPaymentGateway {

    private final MuhouAppProperties properties;

    public MockWechatPaymentGateway(MuhouAppProperties properties) {
        this.properties = properties;
    }

    @Override
    public PaymentCreateResult createPayment(OrderPaymentEntity payment) {
        if (!properties.getPayment().isMockEnabled()) {
            throw new IllegalStateException("Real WeChat payment is not wired yet. Switch muhou.payment.mock-enabled=true for local development.");
        }
        String suffix = payment.getMerchantOutTradeNo();
        return new PaymentCreateResult(
            "mock-prepay-" + suffix,
            "mock-txn-" + suffix,
            "{\"mock\":true,\"merchantOutTradeNo\":\"" + suffix + "\"}",
            true
        );
    }
}
