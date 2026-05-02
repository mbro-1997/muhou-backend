package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.OrderPaymentEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface OrderPaymentMapper {

    @Insert("INSERT INTO order_payment (" +
        "payment_no, order_id, pay_scene, payment_channel, payment_status, merchant_mchid, appid, openid, merchant_out_trade_no, " +
        "wx_transaction_id, wx_prepay_id, description, amount_fen, currency, client_ip, notify_url, attach_data, time_expire, success_time, " +
        "fail_reason, closed_reason, raw_create_response, raw_query_response, created_at, updated_at" +
        ") VALUES (" +
        "#{paymentNo}, #{orderId}, #{payScene}, #{paymentChannel}, #{paymentStatus}, #{merchantMchid}, #{appid}, #{openid}, #{merchantOutTradeNo}, " +
        "#{wxTransactionId}, #{wxPrepayId}, #{description}, #{amountFen}, #{currency}, #{clientIp}, #{notifyUrl}, #{attachData}, #{timeExpire}, #{successTime}, " +
        "#{failReason}, #{closedReason}, #{rawCreateResponse}, #{rawQueryResponse}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderPaymentEntity entity);

    @Select("SELECT * FROM order_payment WHERE id = #{id}")
    OrderPaymentEntity selectById(@Param("id") Long id);

    @Update("UPDATE order_payment SET payment_status = #{paymentStatus}, wx_transaction_id = #{wxTransactionId}, wx_prepay_id = #{wxPrepayId}, success_time = #{successTime}, raw_create_response = #{rawCreateResponse}, updated_at = NOW() WHERE id = #{id}")
    int markSuccess(@Param("id") Long id,
                    @Param("paymentStatus") String paymentStatus,
                    @Param("wxTransactionId") String wxTransactionId,
                    @Param("wxPrepayId") String wxPrepayId,
                    @Param("successTime") LocalDateTime successTime,
                    @Param("rawCreateResponse") String rawCreateResponse);
}
