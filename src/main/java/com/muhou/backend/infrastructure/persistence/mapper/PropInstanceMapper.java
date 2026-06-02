package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.PropInstanceEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PropInstanceMapper {

    @Insert("""
        INSERT INTO prop_instance (
            prop_id, qr_code_id, instance_no, instance_status, current_order_id, remark,
            status_remark, status_changed_at, status_changed_by, created_at, updated_at
        ) VALUES (
            #{propId}, #{qrCodeId}, #{instanceNo}, #{instanceStatus}, #{currentOrderId}, #{remark},
            #{statusRemark}, #{statusChangedAt}, #{statusChangedBy}, NOW(), NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PropInstanceEntity entity);

    @Select("SELECT * FROM prop_instance WHERE id = #{id}")
    PropInstanceEntity selectById(@Param("id") Long id);

    @Select("SELECT * FROM prop_instance WHERE qr_code_id = #{qrCodeId}")
    PropInstanceEntity selectByQrCodeId(@Param("qrCodeId") String qrCodeId);

    @Select("SELECT * FROM prop_instance WHERE prop_id = #{propId} ORDER BY id ASC")
    List<PropInstanceEntity> selectByPropId(@Param("propId") Long propId);

    @Select("SELECT COUNT(1) FROM prop_instance WHERE prop_id = #{propId}")
    int countTotalByPropId(@Param("propId") Long propId);

    @Select("SELECT COUNT(1) FROM prop_instance WHERE prop_id = #{propId} AND instance_status NOT IN ('lost', 'scrapped')")
    int countOperatingByPropId(@Param("propId") Long propId);

    @Select("SELECT COUNT(1) FROM prop_instance WHERE prop_id = #{propId} AND instance_status = #{status}")
    int countByPropIdAndStatus(@Param("propId") Long propId, @Param("status") String status);

    @Select("""
        SELECT id
        FROM prop_instance
        WHERE prop_id = #{propId}
          AND instance_status = 'idle'
        ORDER BY id ASC
        LIMIT ${limit}
        FOR UPDATE
        """)
    List<Long> selectIdleIdsForUpdate(@Param("propId") Long propId, @Param("limit") int limit);

    @Update({
        "<script>",
        "UPDATE prop_instance",
        "SET instance_status = 'locked', current_order_id = #{orderId}, updated_at = NOW()",
        "WHERE id IN",
        "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "AND instance_status = 'idle'",
        "</script>"
    })
    int lockInstances(@Param("ids") List<Long> ids, @Param("orderId") Long orderId);

    @Update("UPDATE prop_instance SET instance_status = 'renting', updated_at = NOW() WHERE id = #{id} AND current_order_id = #{orderId} AND instance_status = 'locked'")
    int markRenting(@Param("id") Long id, @Param("orderId") Long orderId);

    @Update("UPDATE prop_instance SET instance_status = 'idle', current_order_id = NULL, updated_at = NOW() WHERE id = #{id} AND current_order_id = #{orderId} AND instance_status = 'renting'")
    int markIdleReturned(@Param("id") Long id, @Param("orderId") Long orderId);

    @Update("UPDATE prop_instance SET instance_status = 'idle', current_order_id = NULL, updated_at = NOW() WHERE current_order_id = #{orderId} AND instance_status = 'locked'")
    int releaseLockedByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(1) FROM prop_instance WHERE prop_id = #{propId} AND instance_status IN ('locked', 'renting')")
    int countActiveByPropId(@Param("propId") Long propId);

    @Update("""
        UPDATE prop_instance
        SET instance_status = #{targetStatus},
            status_remark = #{reason},
            status_changed_at = NOW(),
            status_changed_by = #{operatorUserId},
            updated_at = NOW()
        WHERE id = #{id}
          AND instance_status = #{fromStatus}
        """)
    int updateStatus(@Param("id") Long id,
                     @Param("fromStatus") String fromStatus,
                     @Param("targetStatus") String targetStatus,
                     @Param("reason") String reason,
                     @Param("operatorUserId") Long operatorUserId);
}
