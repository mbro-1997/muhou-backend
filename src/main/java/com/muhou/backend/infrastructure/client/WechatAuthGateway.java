package com.muhou.backend.infrastructure.client;

public interface WechatAuthGateway {

    AuthLoginResult login(String code);

    AuthLoginResult login(String code, boolean forceReal);
}
