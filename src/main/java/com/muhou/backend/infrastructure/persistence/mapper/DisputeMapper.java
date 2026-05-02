package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.DisputeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DisputeMapper {

    @Select("SELECT d.*, u.nickname AS applicant_name FROM order_dispute d LEFT JOIN sys_user u ON u.id = d.apply_user_id ORDER BY d.created_at DESC, d.id DESC")
    List<DisputeEntity> selectAll();

    @Select("SELECT d.*, u.nickname AS applicant_name FROM order_dispute d LEFT JOIN sys_user u ON u.id = d.apply_user_id WHERE d.id = #{id}")
    DisputeEntity selectById(@Param("id") Long id);

    @Update("UPDATE order_dispute SET dispute_status = 'resolved', resolution = #{resolution}, reviewer_id = #{reviewerId}, resolved_at = NOW() WHERE id = #{id}")
    int resolve(@Param("id") Long id, @Param("resolution") String resolution, @Param("reviewerId") Long reviewerId);
}
