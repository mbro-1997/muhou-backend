package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.UserCreditLogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserCreditLogMapper {

    @Select("""
        SELECT *
        FROM user_credit_log
        WHERE user_id = #{userId}
        ORDER BY created_at DESC, id DESC
        """)
    List<UserCreditLogEntity> selectByUserId(@Param("userId") Long userId);

    @Insert("""
        INSERT INTO user_credit_log (
            user_id, change_type, biz_type, biz_id, before_score, delta_score,
            after_score, reason, operator_user_id, created_at
        ) VALUES (
            #{userId}, #{changeType}, #{bizType}, #{bizId}, #{beforeScore}, #{deltaScore},
            #{afterScore}, #{reason}, #{operatorUserId}, NOW()
        )
        """)
    int insert(UserCreditLogEntity entity);
}
