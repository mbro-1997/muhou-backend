package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.DisputeReasonConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DisputeReasonConfigMapper {

    @Select("""
        SELECT *
        FROM dispute_reason_config
        WHERE stage = #{stage}
          AND applicant_role = #{role}
          AND enabled = 1
        ORDER BY sort_order ASC, id ASC
        """)
    List<DisputeReasonConfigEntity> selectEnabled(@Param("stage") String stage, @Param("role") String role);

    @Select("""
        SELECT *
        FROM dispute_reason_config
        WHERE stage = #{stage}
          AND applicant_role = #{role}
          AND reason_code = #{reasonCode}
          AND enabled = 1
        LIMIT 1
        """)
    DisputeReasonConfigEntity selectEnabledByCode(@Param("stage") String stage,
                                                  @Param("role") String role,
                                                  @Param("reasonCode") String reasonCode);

    @Select("SELECT * FROM dispute_reason_config ORDER BY stage, applicant_role, sort_order, id")
    List<DisputeReasonConfigEntity> selectAll();
}
