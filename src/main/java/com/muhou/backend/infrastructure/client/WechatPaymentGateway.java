package com.muhou.backend.infrastructure.client;

import com.muhou.backend.infrastructure.persistence.entity.OrderPaymentEntity;

public interface WechatPaymentGateway {

    PaymentCreateResult createPayment(OrderPaymentEntity payment);
}
