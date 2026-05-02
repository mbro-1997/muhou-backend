package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.PropEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PropMapper {

    @Select({
        "<script>",
        "SELECT * FROM prop_info",
        "WHERE fill_status = 'filled'",
        "AND audit_status = 'approved'",
        "AND prop_status != 'offline'",
        "<if test='keyword != null and keyword != \"\"'>",
        "AND (prop_name LIKE CONCAT('%', #{keyword}, '%')",
        "OR style_code LIKE CONCAT('%', #{keyword}, '%')",
        "OR type_code LIKE CONCAT('%', #{keyword}, '%'))",
        "</if>",
        "<if test='style != null and style != \"\" and style != \"全部\"'>",
        "AND style_code = #{style}",
        "</if>",
        "<if test='type != null and type != \"\" and type != \"全部\"'>",
        "AND type_code = #{type}",
        "</if>",
        "<if test='status != null and status != \"\"'>",
        "AND prop_status = #{status}",
        "</if>",
        "ORDER BY",
        "<choose>",
        "<when test='sortBy == \"price\"'>daily_rent_price_fen</when>",
        "<otherwise>id</otherwise>",
        "</choose>",
        "<choose>",
        "<when test='sortOrder == \"asc\"'>ASC</when>",
        "<otherwise>DESC</otherwise>",
        "</choose>",
        "</script>"
    })
    List<PropEntity> selectList(@Param("keyword") String keyword,
                                @Param("style") String style,
                                @Param("type") String type,
                                @Param("status") String status,
                                @Param("sortBy") String sortBy,
                                @Param("sortOrder") String sortOrder);

    @Select("SELECT * FROM prop_info WHERE id = #{id}")
    PropEntity selectById(@Param("id") Long id);

    @Select("SELECT * FROM prop_info WHERE supplier_user_id IS NULL AND fill_status = 'pending_fill' ORDER BY created_at DESC, id DESC")
    List<PropEntity> selectPendingFillList();

    @Select("SELECT * FROM prop_info WHERE qr_code_id IS NOT NULL AND qr_code_id != '' ORDER BY created_at DESC, id DESC")
    List<PropEntity> selectQrCodeList();

    @Select("SELECT * FROM prop_info WHERE supplier_user_id = #{supplierUserId} AND fill_status = 'filled' ORDER BY updated_at DESC, id DESC")
    List<PropEntity> selectWarehouseList(@Param("supplierUserId") Long supplierUserId);

    @Select({
        "<script>",
        "SELECT * FROM prop_info WHERE id IN",
        "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    List<PropEntity> selectByIds(@Param("ids") List<Long> ids);

    @Insert("INSERT INTO prop_info (supplier_user_id, prop_name, image_url, style_code, type_code, size_desc, length_cm, width_cm, height_cm, material_desc, daily_rent_price_fen, deposit_amount_fen, fire_resistant_option, weight_desc, transport_suggestion, prop_status, audit_status, fill_status, created_at, updated_at) VALUES (#{supplierUserId}, #{propName}, #{imageUrl}, #{styleCode}, #{typeCode}, #{sizeDesc}, #{lengthCm}, #{widthCm}, #{heightCm}, #{materialDesc}, #{dailyRentPriceFen}, #{depositAmountFen}, #{fireResistantOption}, #{weightDesc}, #{transportSuggestion}, #{propStatus}, #{auditStatus}, #{fillStatus}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PropEntity entity);

    @Insert("INSERT INTO prop_info (supplier_user_id, prop_name, image_url, prop_status, audit_status, qr_code_id, qr_code_url, fill_status, remark, created_at, updated_at) VALUES (#{supplierUserId}, #{propName}, #{imageUrl}, #{propStatus}, #{auditStatus}, #{qrCodeId}, #{qrCodeUrl}, #{fillStatus}, #{remark}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertPendingFill(PropEntity entity);

    @Update("UPDATE prop_info SET supplier_user_id = #{supplierUserId}, prop_name = #{propName}, image_url = #{imageUrl}, style_code = #{styleCode}, type_code = #{typeCode}, size_desc = #{sizeDesc}, length_cm = #{lengthCm}, width_cm = #{widthCm}, height_cm = #{heightCm}, material_desc = #{materialDesc}, daily_rent_price_fen = #{dailyRentPriceFen}, deposit_amount_fen = #{depositAmountFen}, fire_resistant_option = #{fireResistantOption}, weight_desc = #{weightDesc}, transport_suggestion = #{transportSuggestion}, prop_status = #{propStatus}, audit_status = #{auditStatus}, fill_status = #{fillStatus}, remark = #{remark}, updated_at = NOW() WHERE id = #{id}")
    int updatePendingFill(PropEntity entity);

    @Update("UPDATE prop_info SET prop_status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Update("UPDATE prop_info SET audit_status = #{auditStatus}, prop_status = #{propStatus}, updated_at = NOW() WHERE id = #{id}")
    int updateAuditResult(@Param("id") Long id, @Param("auditStatus") String auditStatus, @Param("propStatus") String propStatus);
}
