package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.UserWechatEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserWechatMapper {

    @Select("SELECT * FROM sys_user_wechat WHERE openid = #{openid} LIMIT 1")
    UserWechatEntity selectByOpenid(@Param("openid") String openid);

    @Insert("INSERT INTO sys_user_wechat (user_id, openid, unionid, session_key, created_at, updated_at) VALUES (#{userId}, #{openid}, #{unionid}, #{sessionKey}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserWechatEntity entity);

    @Update("UPDATE sys_user_wechat SET session_key = #{sessionKey}, unionid = #{unionid}, updated_at = NOW() WHERE id = #{id}")
    int updateSession(UserWechatEntity entity);
}
