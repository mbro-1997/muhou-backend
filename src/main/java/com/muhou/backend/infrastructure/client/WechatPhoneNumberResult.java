package com.muhou.backend.infrastructure.client;

public class WechatPhoneNumberResult {

    private final String phoneNumber;
    private final String purePhoneNumber;
    private final String countryCode;

    public WechatPhoneNumberResult(String phoneNumber, String purePhoneNumber, String countryCode) {
        this.phoneNumber = phoneNumber;
        this.purePhoneNumber = purePhoneNumber;
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
