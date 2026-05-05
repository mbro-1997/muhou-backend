package com.muhou.backend.infrastructure.client;

public interface WechatMiniappCodeGateway {

    byte[] generateUnlimited(String scene, String page);
}
