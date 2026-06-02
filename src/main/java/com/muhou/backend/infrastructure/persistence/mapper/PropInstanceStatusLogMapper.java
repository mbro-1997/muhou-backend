package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.PropInstanceStatusLogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PropInstanceStatusLogMapper {

    @Insert("""
        INSERT INTO prop_instance_status_log (
            prop_instance_id, prop_id, instance_no, qr_code_id, from_status, to_status,
            reason, operator_user_id, operator_role, related_order_id, created_at
        ) VALUES (
            #{propInstanceId}, #{propId}, #{instanceNo}, #{qrCodeId}, #{fromStatus}, #{toStatus},
            #{reason}, #{operatorUserId}, #{operatorRole}, #{relatedOrderId}, NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PropInstanceStatusLogEntity entity);

    @Select("SELECT * FROM prop_instance_status_log WHERE prop_instance_id = #{instanceId} ORDER BY created_at DESC, id DESC")
    List<PropInstanceStatusLogEntity> selectByInstanceId(@Param("instanceId") Long instanceId);
}
