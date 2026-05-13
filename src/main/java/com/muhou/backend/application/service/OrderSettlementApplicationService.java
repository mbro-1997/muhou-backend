package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.infrastructure.client.WechatFundResult;
import com.muhou.backend.infrastructure.client.WechatPaymentGateway;
import com.muhou.backend.infrastructure.persistence.entity.DisputeEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderFundFlowEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderEntity;
import com.muhou.backend.infrastructure.persistence.mapper.OrderFundFlowMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PaymentOrderLinkMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderSettlementApplicationService {

    private static final int PLATFORM_COMMISSION_PERCENT = 20;

    private final OrderFundFlowMapper orderFundFlowMapper;
    private final PaymentOrderLinkMapper paymentOrderLinkMapper;
    private final WechatPaymentGateway wechatPaymentGateway;
    private final RentalOrderMapper rentalOrderMapper;

    public OrderSettlementApplicationService(OrderFundFlowMapper orderFundFlowMapper,
                                             PaymentOrderLinkMapper paymentOrderLinkMapper,
                                             WechatPaymentGateway wechatPaymentGateway,
                                             RentalOrderMapper rentalOrderMapper) {
        this.orderFundFlowMapper = orderFundFlowMapper;
        this.paymentOrderLinkMapper = paymentOrderLinkMapper;
        this.wechatPaymentGateway = wechatPaymentGateway;
        this.rentalOrderMapper = rentalOrderMapper;
    }

    @Transactional
    public void settleNormalOrder(RentalOrderEntity order) {
        if (order == null || order.getId() == null || orderFundFlowMapper.countByOrderId(order.getId()) > 0) {
            return;
        }

        int rentFen = positive(order.getRentAmountFen());
        int depositFen = positive(order.getDepositAmountFen());
        int platformFen = calculatePlatformCommission(rentFen);
        int supplierFen = rentFen - platformFen;

        createRefund(order, null, "deposit_refund", order.getDemanderUserId(), depositFen, "正常订单结束，退还押金");
        createProfitShare(order, null, "platform_commission", "platform_admin", null, platformFen, "正常订单结束，平台收取实际费用20%");
        createProfitShare(order, null, "supplier_settlement", "supplier", order.getSupplierUserId(), supplierFen, "正常订单结束，工厂获得实际费用80%");
        paymentOrderLinkMapper.addRefundAmount(order.getId(), depositFen);
        paymentOrderLinkMapper.addSettlementAmount(order.getId(), supplierFen);
        rentalOrderMapper.updateSettlementSummary(order.getId(), "settled", depositFen);
    }

    @Transactional
    public void settleDisputeOrder(RentalOrderEntity order, DisputeEntity dispute, boolean approved) {
        if (order == null || order.getId() == null || dispute == null || orderFundFlowMapper.countByOrderId(order.getId()) > 0) {
            return;
        }
        if (!approved) {
            settleNormalOrder(order);
            return;
        }

        if ("demander".equals(dispute.getApplicantRole())) {
            settleDemanderApproved(order, dispute);
            return;
        }
        if ("supplier".equals(dispute.getApplicantRole())) {
            settleSupplierApproved(order, dispute);
            return;
        }
        throw new BizException(ResultCode.VALIDATION_ERROR, "不支持的仲裁申请方");
    }

    private void settleDemanderApproved(RentalOrderEntity order, DisputeEntity dispute) {
        int rentFen = positive(order.getRentAmountFen());
        int depositFen = positive(order.getDepositAmountFen());
        int compensationFen = Math.min(positive(dispute.getClaimAmountFen()), rentFen);
        int remainingRentFen = rentFen - compensationFen;
        int platformFen = calculatePlatformCommission(remainingRentFen);
        int supplierFen = remainingRentFen - platformFen;

        createRefund(order, dispute, "deposit_refund", order.getDemanderUserId(), depositFen, "用户仲裁工厂成立，退还押金");
        createRefund(order, dispute, "demander_compensation", order.getDemanderUserId(), compensationFen, "用户仲裁工厂成立，从实际费用中赔付用户");
        createProfitShare(order, dispute, "platform_commission", "platform_admin", null, platformFen, "用户仲裁成立后，平台获得剩余实际费用20%");
        createProfitShare(order, dispute, "supplier_settlement", "supplier", order.getSupplierUserId(), supplierFen, "用户仲裁成立后，工厂获得剩余实际费用80%");
        paymentOrderLinkMapper.addRefundAmount(order.getId(), depositFen + compensationFen);
        paymentOrderLinkMapper.addSettlementAmount(order.getId(), supplierFen);
        rentalOrderMapper.updateSettlementSummary(order.getId(), "settled", depositFen + compensationFen);
    }

    private void settleSupplierApproved(RentalOrderEntity order, DisputeEntity dispute) {
        int rentFen = positive(order.getRentAmountFen());
        int depositFen = positive(order.getDepositAmountFen());
        int compensationFen = Math.min(positive(dispute.getClaimAmountFen()), depositFen);
        int userDepositRefundFen = depositFen - compensationFen;
        int platformFen = calculatePlatformCommission(rentFen);
        int supplierRentFen = rentFen - platformFen;

        createRefund(order, dispute, "deposit_refund", order.getDemanderUserId(), userDepositRefundFen, "工厂仲裁用户成立，退还剩余押金");
        createProfitShare(order, dispute, "platform_commission", "platform_admin", null, platformFen, "工厂仲裁成立，平台仍按实际费用20%结算");
        createProfitShare(order, dispute, "supplier_settlement", "supplier", order.getSupplierUserId(), supplierRentFen, "工厂仲裁成立，工厂获得实际费用80%");
        createProfitShare(order, dispute, "supplier_compensation", "supplier", order.getSupplierUserId(), compensationFen, "工厂仲裁用户成立，从押金中赔付工厂");
        paymentOrderLinkMapper.addRefundAmount(order.getId(), userDepositRefundFen);
        paymentOrderLinkMapper.addSettlementAmount(order.getId(), supplierRentFen + compensationFen);
        rentalOrderMapper.updateSettlementSummary(order.getId(), "settled", userDepositRefundFen);
    }

    private void createRefund(RentalOrderEntity order,
                              DisputeEntity dispute,
                              String flowType,
                              Long receiverUserId,
                              int amountFen,
                              String remark) {
        if (amountFen <= 0) {
            return;
        }
        String flowNo = buildFlowNo("RF");
        WechatFundResult result = wechatPaymentGateway.refundToUser(flowNo, order.getId(), receiverUserId, amountFen, remark);
        insertFlow(order, dispute, flowNo, flowType, "platform_to_user", "user", receiverUserId, amountFen, "refund", result, remark);
    }

    private void createProfitShare(RentalOrderEntity order,
                                   DisputeEntity dispute,
                                   String flowType,
                                   String receiverRole,
                                   Long receiverUserId,
                                   int amountFen,
                                   String remark) {
        if (amountFen <= 0) {
            return;
        }
        String flowNo = buildFlowNo("PS");
        WechatFundResult result = wechatPaymentGateway.profitShareToReceiver(flowNo, order.getId(), receiverUserId, receiverRole, amountFen, remark);
        insertFlow(order, dispute, flowNo, flowType, "platform_to_receiver", receiverRole, receiverUserId, amountFen, "profit_sharing", result, remark);
    }

    private void insertFlow(RentalOrderEntity order,
                            DisputeEntity dispute,
                            String flowNo,
                            String flowType,
                            String flowDirection,
                            String receiverRole,
                            Long receiverUserId,
                            int amountFen,
                            String channelAction,
                            WechatFundResult result,
                            String remark) {
        OrderFundFlowEntity entity = new OrderFundFlowEntity();
        entity.setFlowNo(flowNo);
        entity.setOrderId(order.getId());
        entity.setDisputeId(dispute == null ? null : dispute.getId());
        entity.setPaymentId(order.getCurrentPaymentId());
        entity.setFlowType(flowType);
        entity.setFlowDirection(flowDirection);
        entity.setPayerRole("platform");
        entity.setPayerUserId(null);
        entity.setReceiverRole(receiverRole);
        entity.setReceiverUserId(receiverUserId);
        entity.setAmountFen(amountFen);
        entity.setCurrency("CNY");
        entity.setChannelAction(channelAction);
        entity.setChannelStatus("success");
        entity.setWechatOutNo(result.getOutNo());
        entity.setWechatTransactionId(result.getTransactionId());
        entity.setWechatResponse(result.getRawResponse());
        entity.setRemark(remark);
        orderFundFlowMapper.insert(entity);
    }

    private int calculatePlatformCommission(int actualFeeFen) {
        return actualFeeFen * PLATFORM_COMMISSION_PERCENT / 100;
    }

    private int positive(Integer value) {
        return value == null || value < 0 ? 0 : value;
    }

    private String buildFlowNo(String prefix) {
        return prefix + "-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
