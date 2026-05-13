package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.PaymentOrderLinkEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PaymentOrderLinkMapper {

    @Insert({
        "<script>",
        "INSERT INTO payment_order_link (",
        "payment_id, order_id, order_no, supplier_user_id, rent_amount_fen, deposit_amount_fen, total_amount_fen, refund_amount_fen, settlement_amount_fen, created_at, updated_at",
        ") VALUES ",
        "<foreach collection='items' item='item' separator=','>",
        "(",
        "#{item.paymentId}, #{item.orderId}, #{item.orderNo}, #{item.supplierUserId}, #{item.rentAmountFen}, #{item.depositAmountFen}, #{item.totalAmountFen}, ",
        "#{item.refundAmountFen}, #{item.settlementAmountFen}, NOW(), NOW()",
        ")",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("items") List<PaymentOrderLinkEntity> items);

    @Select("SELECT * FROM payment_order_link WHERE payment_id = #{paymentId} ORDER BY id ASC")
    List<PaymentOrderLinkEntity> selectByPaymentId(@Param("paymentId") Long paymentId);

    @Select("SELECT * FROM payment_order_link WHERE order_id = #{orderId} LIMIT 1")
    PaymentOrderLinkEntity selectByOrderId(@Param("orderId") Long orderId);

    @Update("UPDATE payment_order_link SET refund_amount_fen = refund_amount_fen + #{amountFen}, updated_at = NOW() WHERE order_id = #{orderId}")
    int addRefundAmount(@Param("orderId") Long orderId, @Param("amountFen") Integer amountFen);

    @Update("UPDATE payment_order_link SET settlement_amount_fen = settlement_amount_fen + #{amountFen}, updated_at = NOW() WHERE order_id = #{orderId}")
    int addSettlementAmount(@Param("orderId") Long orderId, @Param("amountFen") Integer amountFen);
}
