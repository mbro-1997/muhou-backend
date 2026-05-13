package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.DisputeEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DisputeMapper {

    @Insert("INSERT INTO order_dispute (" +
        "order_id, apply_user_id, applicant_role, apply_stage, title, content, claim_amount_fen, deposit_amount_fen_snapshot, order_status_snapshot, demander_user_id, supplier_user_id, evidence_urls, dispute_status, created_at" +
        ") VALUES (" +
        "#{orderId}, #{applyUserId}, #{applicantRole}, #{applyStage}, #{title}, #{content}, #{claimAmountFen}, #{depositAmountFenSnapshot}, #{orderStatusSnapshot}, #{demanderUserId}, #{supplierUserId}, #{evidenceUrls}, #{disputeStatus}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DisputeEntity entity);

    @Select("SELECT COUNT(1) FROM order_dispute WHERE order_id = #{orderId} AND apply_user_id = #{applyUserId} AND applicant_role = #{applicantRole}")
    int countByOrderAndApplicant(@Param("orderId") Long orderId,
                                 @Param("applyUserId") Long applyUserId,
                                 @Param("applicantRole") String applicantRole);

    @Select("SELECT COUNT(1) FROM order_dispute WHERE order_id = #{orderId}")
    int countByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(1) FROM order_dispute WHERE order_id = #{orderId} AND dispute_status = 'pending'")
    int countPendingByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT d.*, u.nickname AS applicant_name, o.order_no AS order_no FROM order_dispute d LEFT JOIN sys_user u ON u.id = d.apply_user_id LEFT JOIN rental_order o ON o.id = d.order_id ORDER BY d.created_at DESC, d.id DESC")
    List<DisputeEntity> selectAll();

    @Select("SELECT d.*, u.nickname AS applicant_name, o.order_no AS order_no FROM order_dispute d LEFT JOIN sys_user u ON u.id = d.apply_user_id LEFT JOIN rental_order o ON o.id = d.order_id WHERE d.order_id = #{orderId} ORDER BY d.created_at DESC, d.id DESC")
    List<DisputeEntity> selectByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT d.*, u.nickname AS applicant_name, o.order_no AS order_no FROM order_dispute d LEFT JOIN sys_user u ON u.id = d.apply_user_id LEFT JOIN rental_order o ON o.id = d.order_id WHERE d.apply_user_id = #{userId} OR d.demander_user_id = #{userId} OR d.supplier_user_id = #{userId} ORDER BY d.created_at DESC, d.id DESC")
    List<DisputeEntity> selectByParticipantUserId(@Param("userId") Long userId);

    @Select("SELECT d.*, u.nickname AS applicant_name, o.order_no AS order_no FROM order_dispute d LEFT JOIN sys_user u ON u.id = d.apply_user_id LEFT JOIN rental_order o ON o.id = d.order_id WHERE d.id = #{id}")
    DisputeEntity selectById(@Param("id") Long id);

    @Update("UPDATE order_dispute SET dispute_status = #{status}, resolution = #{resolution}, resolution_type = #{resolutionType}, refund_status = #{refundStatus}, reviewer_id = #{reviewerId}, resolved_at = NOW() WHERE id = #{id} AND dispute_status = 'pending'")
    int resolve(@Param("id") Long id,
                @Param("status") String status,
                @Param("resolution") String resolution,
                @Param("resolutionType") String resolutionType,
                @Param("refundStatus") String refundStatus,
                @Param("reviewerId") Long reviewerId);
}
