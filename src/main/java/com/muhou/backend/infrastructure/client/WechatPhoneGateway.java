package com.muhou.backend.infrastructure.client;

public interface WechatPhoneGateway {

    WechatPhoneNumberResult getPhoneNumber(String code);
}
