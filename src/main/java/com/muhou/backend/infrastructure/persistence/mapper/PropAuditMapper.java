package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.PropAuditEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PropAuditMapper {

    @Select("SELECT pa.*, p.prop_name, p.image_url, p.style_code, p.type_code, p.size_desc, p.length_cm, p.width_cm, p.height_cm, p.material_desc, p.daily_rent_price_fen, p.deposit_amount_fen, p.fire_resistant_option, p.weight_desc, p.transport_suggestion, p.prop_status, p.audit_status AS prop_audit_status, p.fill_status, p.qr_code_id, p.supplier_user_id, u.nickname AS supplier_nickname, u.phone AS supplier_phone FROM prop_audit pa LEFT JOIN prop_info p ON p.id = pa.prop_id LEFT JOIN sys_user u ON u.id = p.supplier_user_id WHERE pa.audit_status = 'pending' ORDER BY pa.submitted_at DESC, pa.id DESC")
    List<PropAuditEntity> selectPendingList();

    @Select("SELECT pa.*, p.prop_name, p.image_url, p.style_code, p.type_code, p.size_desc, p.length_cm, p.width_cm, p.height_cm, p.material_desc, p.daily_rent_price_fen, p.deposit_amount_fen, p.fire_resistant_option, p.weight_desc, p.transport_suggestion, p.prop_status, p.audit_status AS prop_audit_status, p.fill_status, p.qr_code_id, p.supplier_user_id, u.nickname AS supplier_nickname, u.phone AS supplier_phone FROM prop_audit pa LEFT JOIN prop_info p ON p.id = pa.prop_id LEFT JOIN sys_user u ON u.id = p.supplier_user_id WHERE pa.audit_status <> 'pending' ORDER BY pa.reviewed_at DESC, pa.id DESC")
    List<PropAuditEntity> selectHistoryList();

    @Select("SELECT pa.*, p.prop_name, p.image_url, p.style_code, p.type_code, p.size_desc, p.length_cm, p.width_cm, p.height_cm, p.material_desc, p.daily_rent_price_fen, p.deposit_amount_fen, p.fire_resistant_option, p.weight_desc, p.transport_suggestion, p.prop_status, p.audit_status AS prop_audit_status, p.fill_status, p.qr_code_id, p.supplier_user_id, u.nickname AS supplier_nickname, u.phone AS supplier_phone FROM prop_audit pa LEFT JOIN prop_info p ON p.id = pa.prop_id LEFT JOIN sys_user u ON u.id = p.supplier_user_id WHERE pa.id = #{id}")
    PropAuditEntity selectById(@Param("id") Long id);

    @Insert("INSERT INTO prop_audit (prop_id, action_type, audit_status, apply_remark, submitted_at) VALUES (#{propId}, #{actionType}, #{auditStatus}, #{applyRemark}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PropAuditEntity entity);

    @Update("UPDATE prop_audit SET audit_status = #{status}, audit_remark = #{remark}, reviewer_id = #{reviewerId}, reviewed_at = NOW() WHERE id = #{id}")
    int updateReview(@Param("id") Long id, @Param("status") String status, @Param("remark") String remark, @Param("reviewerId") Long reviewerId);
}
