package com.muhou.backend.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT role_code FROM sys_user_role WHERE user_id = #{userId} AND enabled = 1 ORDER BY id")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO sys_user_role (user_id, role_code, enabled, created_at) VALUES (#{userId}, #{roleCode}, 1, NOW()) ON DUPLICATE KEY UPDATE enabled = 1")
    int insertIgnore(@Param("userId") Long userId, @Param("roleCode") String roleCode);

    @Update("UPDATE sys_user_role SET enabled = 0 WHERE user_id = #{userId} AND role_code = #{roleCode} AND enabled = 1")
    int disableRole(@Param("userId") Long userId, @Param("roleCode") String roleCode);

    @Update("UPDATE sys_user_role SET enabled = 0 WHERE user_id = #{userId} AND enabled = 1")
    int disableAllRoles(@Param("userId") Long userId);
}
