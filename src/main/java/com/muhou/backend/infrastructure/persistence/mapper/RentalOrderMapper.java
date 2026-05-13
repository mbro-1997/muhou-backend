package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.RentalOrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RentalOrderMapper {

    @Insert("INSERT INTO rental_order (" +
        "order_no, project_id, demander_user_id, supplier_user_id, order_status, rental_days, rental_start_date, rental_end_date, contact_address, use_scene, special_remark, rent_amount_fen, deposit_amount_fen, total_amount_fen, " +
        "pay_status, refund_status, total_paid_fen, total_refunded_fen, confirm_deadline_at, remark, created_by, created_at, updated_at" +
        ") VALUES (" +
        "#{orderNo}, #{projectId}, #{demanderUserId}, #{supplierUserId}, #{orderStatus}, #{rentalDays}, #{rentalStartDate}, #{rentalEndDate}, #{contactAddress}, #{useScene}, #{specialRemark}, #{rentAmountFen}, #{depositAmountFen}, #{totalAmountFen}, " +
        "#{payStatus}, #{refundStatus}, #{totalPaidFen}, #{totalRefundedFen}, #{confirmDeadlineAt}, #{remark}, #{createdBy}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RentalOrderEntity entity);

    @Select("SELECT * FROM rental_order WHERE id = #{id}")
    RentalOrderEntity selectById(@Param("id") Long id);

    @Select({
        "<script>",
        "SELECT * FROM rental_order",
        "WHERE",
        "<choose>",
        "<when test='role == \"supplier\"'>supplier_user_id = #{userId}</when>",
        "<otherwise>demander_user_id = #{userId}</otherwise>",
        "</choose>",
        "ORDER BY created_at DESC, id DESC",
        "</script>"
    })
    List<RentalOrderEntity> selectByRole(@Param("role") String role, @Param("userId") Long userId);

    @Select("SELECT * FROM rental_order WHERE order_status = 'pending_factory_confirm' AND pay_status = 'paid' AND confirm_deadline_at IS NOT NULL AND confirm_deadline_at <= #{now}")
    List<RentalOrderEntity> selectTimeoutPendingConfirmOrders(@Param("now") LocalDateTime now);

    @Select("SELECT * FROM rental_order WHERE order_status = 'wait_review' AND returned_at IS NOT NULL AND returned_at <= #{deadline}")
    List<RentalOrderEntity> selectWaitReviewExpiredOrders(@Param("deadline") LocalDateTime deadline);

    @Update("UPDATE rental_order SET current_payment_id = #{paymentId}, pay_status = #{payStatus}, total_paid_fen = #{totalPaidFen}, paid_at = #{paidAt}, updated_at = NOW() WHERE id = #{orderId}")
    int markPaid(@Param("orderId") Long orderId,
                 @Param("paymentId") Long paymentId,
                 @Param("payStatus") String payStatus,
                 @Param("totalPaidFen") Integer totalPaidFen,
                 @Param("paidAt") LocalDateTime paidAt);

    @Update("UPDATE rental_order SET order_status = 'wait_pickup', confirmed_at = #{confirmedAt}, updated_at = NOW() WHERE id = #{orderId}")
    int markFactoryConfirmed(@Param("orderId") Long orderId, @Param("confirmedAt") LocalDateTime confirmedAt);

    @Update("UPDATE rental_order SET order_status = 'renting', picked_up_at = #{pickedUpAt}, updated_at = NOW() WHERE id = #{orderId}")
    int markPickedUp(@Param("orderId") Long orderId, @Param("pickedUpAt") LocalDateTime pickedUpAt);

    @Update("UPDATE rental_order SET order_status = 'wait_review', returned_at = #{returnedAt}, updated_at = NOW() WHERE id = #{orderId}")
    int markReturned(@Param("orderId") Long orderId, @Param("returnedAt") LocalDateTime returnedAt);

    @Update("UPDATE rental_order SET order_status = 'completed', reviewed_at = #{reviewedAt}, deposit_refunded_flag = 1, updated_at = NOW() WHERE id = #{orderId}")
    int markReviewed(@Param("orderId") Long orderId, @Param("reviewedAt") LocalDateTime reviewedAt);

    @Update("UPDATE rental_order SET refund_status = #{refundStatus}, total_refunded_fen = #{totalRefundedFen}, updated_at = NOW() WHERE id = #{orderId}")
    int updateSettlementSummary(@Param("orderId") Long orderId,
                                @Param("refundStatus") String refundStatus,
                                @Param("totalRefundedFen") Integer totalRefundedFen);

    @Update("UPDATE rental_order SET order_status = 'cancelled_timeout', cancelled_at = #{cancelledAt}, cancel_reason = #{cancelReason}, updated_at = NOW() WHERE id = #{orderId} AND order_status = 'pending_factory_confirm'")
    int markTimeoutCancelled(@Param("orderId") Long orderId,
                             @Param("cancelledAt") LocalDateTime cancelledAt,
                             @Param("cancelReason") String cancelReason);

    @Update("UPDATE rental_order SET order_status = 'cancelled_manual', cancelled_at = #{cancelledAt}, cancel_reason = #{cancelReason}, updated_at = NOW() WHERE id = #{orderId} AND order_status = 'pending_factory_confirm'")
    int markFactoryRejected(@Param("orderId") Long orderId,
                            @Param("cancelledAt") LocalDateTime cancelledAt,
                            @Param("cancelReason") String cancelReason);
}
