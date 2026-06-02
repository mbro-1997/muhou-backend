package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.AdminAccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdminAccountMapper {

    @Select("SELECT * FROM admin_account WHERE username = #{username} LIMIT 1")
    AdminAccountEntity selectByUsername(@Param("username") String username);

    @Update("UPDATE admin_account SET last_login_at = NOW(), updated_at = NOW() WHERE id = #{id}")
    int updateLastLogin(@Param("id") Long id);
}
