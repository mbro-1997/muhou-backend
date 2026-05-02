package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT u.* FROM sys_user u JOIN sys_user_role r ON u.id = r.user_id WHERE r.role_code = #{role} AND r.enabled = 1 ORDER BY u.id LIMIT 1")
    UserEntity selectFirstByRole(@Param("role") String role);

    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    UserEntity selectById(@Param("id") Long id);

    @Select("SELECT u.*, (SELECT role_code FROM sys_user_role r WHERE r.user_id = u.id AND r.enabled = 1 ORDER BY r.id LIMIT 1) AS primary_role FROM sys_user u ORDER BY u.id")
    List<UserEntity> selectAdminList();

    @Insert("INSERT INTO sys_user (nickname, avatar_url, phone, realname_verified, student_verified, user_status, register_status, created_at, updated_at) VALUES (#{nickname}, #{avatarUrl}, #{phone}, #{realnameVerified}, #{studentVerified}, #{userStatus}, #{registerStatus}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserEntity entity);

    @Update("UPDATE sys_user SET register_status = #{registerStatus}, updated_at = NOW() WHERE id = #{id}")
    int updateRegisterStatus(@Param("id") Long id, @Param("registerStatus") String registerStatus);
}
