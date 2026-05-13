package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.OrderFundFlowEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderFundFlowMapper {

    @Insert("""
        INSERT INTO order_fund_flow (
            flow_no, order_id, dispute_id, payment_id, flow_type, flow_direction,
            payer_role, payer_user_id, receiver_role, receiver_user_id, amount_fen,
            currency, channel_action, channel_status, wechat_out_no,
            wechat_transaction_id, wechat_response, remark, created_at, updated_at
        ) VALUES (
            #{flowNo}, #{orderId}, #{disputeId}, #{paymentId}, #{flowType}, #{flowDirection},
            #{payerRole}, #{payerUserId}, #{receiverRole}, #{receiverUserId}, #{amountFen},
            #{currency}, #{channelAction}, #{channelStatus}, #{wechatOutNo},
            #{wechatTransactionId}, #{wechatResponse}, #{remark}, NOW(), NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderFundFlowEntity entity);

    @Select("SELECT COUNT(1) FROM order_fund_flow WHERE order_id = #{orderId}")
    int countByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT * FROM order_fund_flow WHERE order_id = #{orderId} ORDER BY id ASC")
    List<OrderFundFlowEntity> selectByOrderId(@Param("orderId") Long orderId);
}
