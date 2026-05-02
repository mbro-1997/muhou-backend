package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.FactoryAuditEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FactoryAuditMapper {

    @Select("SELECT * FROM supplier_settlement_audit ORDER BY submitted_at DESC, id DESC")
    List<FactoryAuditEntity> selectAll();

    @Select("SELECT * FROM supplier_settlement_audit WHERE audit_status = 'pending' ORDER BY submitted_at DESC, id DESC")
    List<FactoryAuditEntity> selectPendingList();

    @Select("SELECT * FROM supplier_settlement_audit WHERE audit_status <> 'pending' ORDER BY reviewed_at DESC, submitted_at DESC, id DESC")
    List<FactoryAuditEntity> selectHistoryList();

    @Select("SELECT * FROM supplier_settlement_audit WHERE id = #{id}")
    FactoryAuditEntity selectById(@Param("id") Long id);

    @Select("SELECT * FROM supplier_settlement_audit WHERE applicant_user_id = #{userId} ORDER BY id DESC LIMIT 1")
    FactoryAuditEntity selectLatestByApplicantUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(1) FROM supplier_settlement_audit WHERE applicant_user_id = #{userId} AND audit_status = 'pending'")
    int countPendingByApplicantUserId(@Param("userId") Long userId);

    @Insert("""
        INSERT INTO supplier_settlement_audit
        (user_id, applicant_user_id, invite_code_id, company_name, factory_name, unified_social_credit_code,
         business_license_url, contact_name, contact_phone, factory_address, main_business, remark,
         audit_status, submitted_at, updated_at)
        VALUES
        (#{userId}, #{applicantUserId}, #{inviteCodeId}, #{companyName}, #{factoryName}, #{unifiedSocialCreditCode},
         #{businessLicenseUrl}, #{contactName}, #{contactPhone}, #{factoryAddress}, #{mainBusiness}, #{remark},
         #{auditStatus}, NOW(), NOW())
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FactoryAuditEntity entity);

    @Update("""
        UPDATE supplier_settlement_audit
        SET audit_status = #{status},
            audit_remark = #{remark},
            reject_reason = #{rejectReason},
            reviewer_id = #{reviewerId},
            reviewed_by_admin_user_id = #{reviewerId},
            reviewed_at = NOW(),
            created_factory_id = #{createdFactoryId},
            updated_at = NOW()
        WHERE id = #{id} AND audit_status = 'pending'
        """)
    int updateReview(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("remark") String remark,
                     @Param("rejectReason") String rejectReason,
                     @Param("reviewerId") Long reviewerId,
                     @Param("createdFactoryId") Long createdFactoryId);

    @Delete("DELETE FROM supplier_settlement_audit WHERE applicant_user_id = #{userId}")
    int deleteByApplicantUserId(@Param("userId") Long userId);
}
