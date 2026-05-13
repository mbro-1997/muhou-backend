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

    @Override
    public WechatFundResult refundToUser(String outRefundNo, Long orderId, Long receiverUserId, int amountFen, String reason) {
        if (!properties.getPayment().isMockEnabled()) {
            throw new IllegalStateException("Real WeChat refund is not wired yet. Switch muhou.payment.mock-enabled=true for local development.");
        }
        return new WechatFundResult(
            outRefundNo,
            "mock-refund-" + outRefundNo,
            "{\"mock\":true,\"action\":\"refund\",\"outRefundNo\":\"" + outRefundNo + "\",\"amountFen\":" + amountFen + "}",
            true
        );
    }

    @Override
    public WechatFundResult profitShareToReceiver(String outOrderNo, Long orderId, Long receiverUserId, String receiverRole, int amountFen, String description) {
        if (!properties.getPayment().isMockEnabled()) {
            throw new IllegalStateException("Real WeChat profit sharing is not wired yet. Switch muhou.payment.mock-enabled=true for local development.");
        }
        return new WechatFundResult(
            outOrderNo,
            "mock-profit-sharing-" + outOrderNo,
            "{\"mock\":true,\"action\":\"profit_sharing\",\"outOrderNo\":\"" + outOrderNo + "\",\"receiverRole\":\"" + receiverRole + "\",\"amountFen\":" + amountFen + "}",
            true
        );
    }
}
