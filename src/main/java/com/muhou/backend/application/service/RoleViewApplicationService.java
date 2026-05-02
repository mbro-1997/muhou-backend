package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import com.muhou.backend.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleViewApplicationService {

    private final UserMapper userMapper;

    public RoleViewApplicationService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Long resolveRequiredUserId(String role) {
        UserEntity user = userMapper.selectFirstByRole(role);
        if (user == null || user.getId() == null) {
            throw new BizException(ResultCode.NOT_FOUND, "未找到可用于该角色视角的测试账号: " + role);
        }
        return user.getId();
    }
}
