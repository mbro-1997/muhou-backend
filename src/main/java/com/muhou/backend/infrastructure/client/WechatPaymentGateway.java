package com.muhou.backend.infrastructure.client;

import com.muhou.backend.infrastructure.persistence.entity.OrderPaymentEntity;

public interface WechatPaymentGateway {

    PaymentCreateResult createPayment(OrderPaymentEntity payment);

    WechatFundResult refundToUser(String outRefundNo,
                                  Long orderId,
                                  Long receiverUserId,
                                  int amountFen,
                                  String reason);

    WechatFundResult profitShareToReceiver(String outOrderNo,
                                           Long orderId,
                                           Long receiverUserId,
                                           String receiverRole,
                                           int amountFen,
                                           String description);
}
