package com.muhou.backend.infrastructure.client;

public class WechatFundResult {

    private final String outNo;
    private final String transactionId;
    private final String rawResponse;
    private final boolean mock;

    public WechatFundResult(String outNo, String transactionId, String rawResponse, boolean mock) {
        this.outNo = outNo;
        this.transactionId = transactionId;
        this.rawResponse = rawResponse;
        this.mock = mock;
    }

    public String getOutNo() {
        return outNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public boolean isMock() {
        return mock;
    }
}
