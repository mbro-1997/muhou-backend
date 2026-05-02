package com.muhou.backend.infrastructure.client;

public class PaymentCreateResult {

    private final String prepayId;
    private final String transactionId;
    private final String rawResponse;
    private final boolean mock;

    public PaymentCreateResult(String prepayId, String transactionId, String rawResponse, boolean mock) {
        this.prepayId = prepayId;
        this.transactionId = transactionId;
        this.rawResponse = rawResponse;
        this.mock = mock;
    }

    public String getPrepayId() {
        return prepayId;
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
