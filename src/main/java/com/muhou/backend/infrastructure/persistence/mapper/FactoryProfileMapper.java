package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.FactoryProfileEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FactoryProfileMapper {

    @Select("SELECT * FROM factory_profile WHERE owner_user_id = #{ownerUserId}")
    FactoryProfileEntity selectByOwnerUserId(@Param("ownerUserId") Long ownerUserId);

    @Select("SELECT * FROM factory_profile WHERE unified_social_credit_code = #{creditCode}")
    FactoryProfileEntity selectByUnifiedSocialCreditCode(@Param("creditCode") String creditCode);

    @Insert("""
        INSERT INTO factory_profile
        (owner_user_id, factory_name, unified_social_credit_code, business_license_url, contact_name, contact_phone,
         factory_address, main_business, status, approved_audit_id, approved_by_admin_user_id, approved_at, created_at, updated_at)
        VALUES
        (#{ownerUserId}, #{factoryName}, #{unifiedSocialCreditCode}, #{businessLicenseUrl}, #{contactName}, #{contactPhone},
         #{factoryAddress}, #{mainBusiness}, #{status}, #{approvedAuditId}, #{approvedByAdminUserId}, #{approvedAt}, NOW(), NOW())
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FactoryProfileEntity entity);

    @Delete("DELETE FROM factory_profile WHERE owner_user_id = #{ownerUserId}")
    int deleteByOwnerUserId(@Param("ownerUserId") Long ownerUserId);
}
