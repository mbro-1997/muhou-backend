package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.FactoryInviteCodeEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FactoryInviteCodeMapper {

    @Select("SELECT * FROM factory_invite_code ORDER BY created_at DESC, id DESC")
    List<FactoryInviteCodeEntity> selectAll();

    @Select("SELECT * FROM factory_invite_code WHERE id = #{id}")
    FactoryInviteCodeEntity selectById(@Param("id") Long id);

    @Select("SELECT * FROM factory_invite_code WHERE code_hash = #{codeHash} LIMIT 1")
    FactoryInviteCodeEntity selectByCodeHash(@Param("codeHash") String codeHash);

    @Insert("""
        INSERT INTO factory_invite_code
        (code_hash, code_plain, code_suffix, status, expire_at, created_by_admin_user_id, remark, created_at, updated_at)
        VALUES
        (#{codeHash}, #{codePlain}, #{codeSuffix}, #{status}, #{expireAt}, #{createdByAdminUserId}, #{remark}, NOW(), NOW())
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FactoryInviteCodeEntity entity);

    @Update("""
        UPDATE factory_invite_code
        SET status = 'locked',
            locked_by_user_id = #{userId},
            locked_at = NOW(),
            updated_at = NOW()
        WHERE id = #{id}
          AND status = 'unused'
        """)
    int lockByUser(@Param("id") Long id, @Param("userId") Long userId);

    @Update("""
        UPDATE factory_invite_code
        SET status = 'used',
            used_by_user_id = #{userId},
            used_at = NOW(),
            updated_at = NOW()
        WHERE id = #{id}
        """)
    int markUsed(@Param("id") Long id, @Param("userId") Long userId);

    @Update("""
        UPDATE factory_invite_code
        SET status = 'revoked',
            revoked_by_admin_user_id = #{adminUserId},
            revoked_at = NOW(),
            updated_at = NOW()
        WHERE id = #{id}
          AND status IN ('unused', 'locked')
        """)
    int revoke(@Param("id") Long id, @Param("adminUserId") Long adminUserId);
}
