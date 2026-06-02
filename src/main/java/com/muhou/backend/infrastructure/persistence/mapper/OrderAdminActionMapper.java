package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.OrderAdminActionEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderAdminActionMapper {

    @Insert("""
        INSERT INTO order_admin_action (
            order_id, dispute_id, action_type, action_status, before_order_status, after_order_status,
            before_pay_status, after_pay_status, amount_fen, target_role, target_user_id, score_delta,
            reason, internal_note, operator_user_id, created_at
        ) VALUES (
            #{orderId}, #{disputeId}, #{actionType}, #{actionStatus}, #{beforeOrderStatus}, #{afterOrderStatus},
            #{beforePayStatus}, #{afterPayStatus}, #{amountFen}, #{targetRole}, #{targetUserId}, #{scoreDelta},
            #{reason}, #{internalNote}, #{operatorUserId}, NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderAdminActionEntity entity);

    @Select("""
        SELECT a.*, o.order_no AS order_no, u.nickname AS operator_name
        FROM order_admin_action a
        LEFT JOIN rental_order o ON o.id = a.order_id
        LEFT JOIN sys_user u ON u.id = a.operator_user_id
        WHERE a.order_id = #{orderId}
        ORDER BY a.created_at DESC, a.id DESC
        """)
    List<OrderAdminActionEntity> selectByOrderId(@Param("orderId") Long orderId);

    @Select("""
        <script>
        SELECT a.*, o.order_no AS order_no, u.nickname AS operator_name
        FROM order_admin_action a
        LEFT JOIN rental_order o ON o.id = a.order_id
        LEFT JOIN sys_user u ON u.id = a.operator_user_id
        WHERE 1 = 1
        <if test='orderNo != null and orderNo != ""'>AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if>
        <if test='actionType != null and actionType != ""'>AND a.action_type = #{actionType}</if>
        <if test='targetRole != null and targetRole != ""'>AND a.target_role = #{targetRole}</if>
        <if test='targetUserId != null'>AND a.target_user_id = #{targetUserId}</if>
        <if test='operatorUserId != null'>AND a.operator_user_id = #{operatorUserId}</if>
        ORDER BY a.created_at DESC, a.id DESC
        LIMIT #{limit} OFFSET #{offset}
        </script>
        """)
    List<OrderAdminActionEntity> selectAdminPage(@Param("orderNo") String orderNo,
                                                 @Param("actionType") String actionType,
                                                 @Param("operatorUserId") Long operatorUserId,
                                                 @Param("targetRole") String targetRole,
                                                 @Param("targetUserId") Long targetUserId,
                                                 @Param("limit") int limit,
                                                 @Param("offset") int offset);
}
