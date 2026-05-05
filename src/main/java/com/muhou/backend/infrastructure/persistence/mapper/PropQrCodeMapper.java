package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.PropQrCodeEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PropQrCodeMapper {

    @Insert("""
        INSERT INTO prop_qr_code (
            prop_id, qr_code_id, qr_scene, qr_page, qr_image_url, qr_image_storage_key,
            image_sha256, status, created_by_admin_user_id, created_at, updated_at
        ) VALUES (
            #{propId}, #{qrCodeId}, #{qrScene}, #{qrPage}, #{qrImageUrl}, #{qrImageStorageKey},
            #{imageSha256}, #{status}, #{createdByAdminUserId}, NOW(), NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PropQrCodeEntity entity);

    @Select("SELECT * FROM prop_qr_code WHERE qr_code_id = #{qrCodeId}")
    PropQrCodeEntity selectByQrCodeId(@Param("qrCodeId") String qrCodeId);

    @Select("SELECT * FROM prop_qr_code WHERE prop_id = #{propId}")
    PropQrCodeEntity selectByPropId(@Param("propId") Long propId);

    @Select("SELECT * FROM prop_qr_code ORDER BY created_at DESC, id DESC")
    List<PropQrCodeEntity> selectAll();

    @Update("""
        UPDATE prop_qr_code
        SET status = 'filled',
            used_by_supplier_user_id = #{supplierUserId},
            used_at = NOW(),
            updated_at = NOW()
        WHERE prop_id = #{propId}
          AND status = 'unused'
        """)
    int markFilled(@Param("propId") Long propId, @Param("supplierUserId") Long supplierUserId);

    @Update("""
        UPDATE prop_qr_code
        SET status = 'revoked',
            revoked_by_admin_user_id = #{adminUserId},
            revoked_at = NOW(),
            updated_at = NOW()
        WHERE prop_id = #{propId}
          AND status = 'unused'
        """)
    int revokeByPropId(@Param("propId") Long propId, @Param("adminUserId") Long adminUserId);

    @Update("""
        UPDATE prop_qr_code
        SET downloaded_at = NOW(),
            updated_at = NOW()
        WHERE qr_code_id = #{qrCodeId}
        """)
    int markDownloaded(@Param("qrCodeId") String qrCodeId);
}
