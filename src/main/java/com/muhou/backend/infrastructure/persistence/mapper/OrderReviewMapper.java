package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.AdminReviewEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderReviewEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderReviewMapper {

    @Insert("INSERT INTO order_review (order_id, reviewer_user_id, reviewer_role, score, prop_score, counterparty_score, content, visible_flag, auto_flag, created_at) " +
        "VALUES (#{orderId}, #{reviewerUserId}, #{reviewerRole}, #{score}, #{propScore}, #{counterpartyScore}, #{content}, #{visibleFlag}, #{autoFlag}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderReviewEntity entity);

    @Select("SELECT * FROM order_review WHERE order_id = #{orderId} AND reviewer_role = #{reviewerRole} LIMIT 1")
    OrderReviewEntity selectByOrderIdAndRole(@Param("orderId") Long orderId, @Param("reviewerRole") String reviewerRole);

    @Select("SELECT COUNT(1) FROM order_review WHERE order_id = #{orderId}")
    int countByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT r.id, r.order_id, o.order_no, r.reviewer_user_id, u.nickname AS reviewer_name, r.reviewer_role, r.score, r.prop_score, r.counterparty_score, r.content, r.visible_flag, r.auto_flag, r.created_at " +
        "FROM order_review r " +
        "LEFT JOIN rental_order o ON o.id = r.order_id " +
        "LEFT JOIN sys_user u ON u.id = r.reviewer_user_id " +
        "ORDER BY r.created_at DESC, r.id DESC")
    List<AdminReviewEntity> selectAdminList();

    @Update("UPDATE order_review SET visible_flag = #{visibleFlag} WHERE id = #{id}")
    int updateVisibleFlag(@Param("id") Long id, @Param("visibleFlag") Integer visibleFlag);

    @Delete("DELETE FROM order_review WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
