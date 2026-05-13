package com.muhou.backend.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "muhou")
public class MuhouAppProperties {

    private final Auth auth = new Auth();
    private final Payment payment = new Payment();
    private final Wechat wechat = new Wechat();
    private final Order order = new Order();

    public Auth getAuth() {
        return auth;
    }

    public Payment getPayment() {
        return payment;
    }

    public Wechat getWechat() {
        return wechat;
    }

    public Order getOrder() {
        return order;
    }

    public static class Auth {
        private boolean demoLoginEnabled = true;
        private boolean allowProfileRoleFallback = false;
        private String tokenSecret = "muhou-dev-token-secret";
        private long tokenTtlMs = 604800000L;

        public boolean isDemoLoginEnabled() {
            return demoLoginEnabled;
        }

        public void setDemoLoginEnabled(boolean demoLoginEnabled) {
            this.demoLoginEnabled = demoLoginEnabled;
        }

        public boolean isAllowProfileRoleFallback() {
            return allowProfileRoleFallback;
        }

        public void setAllowProfileRoleFallback(boolean allowProfileRoleFallback) {
            this.allowProfileRoleFallback = allowProfileRoleFallback;
        }

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenTtlMs() {
            return tokenTtlMs;
        }

        public void setTokenTtlMs(long tokenTtlMs) {
            this.tokenTtlMs = tokenTtlMs;
        }
    }

    public static class Payment {
        private boolean mockEnabled = true;
        private String mchId;
        private String appId;
        private String privateKeyPath;
        private String merchantSerialNo;
        private String apiV3Key;
        private String notifyUrl;
        private String refundNotifyUrl;

        public boolean isMockEnabled() {
            return mockEnabled;
        }

        public void setMockEnabled(boolean mockEnabled) {
            this.mockEnabled = mockEnabled;
        }

        public String getMchId() {
            return mchId;
        }

        public void setMchId(String mchId) {
            this.mchId = mchId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getPrivateKeyPath() {
            return privateKeyPath;
        }

        public void setPrivateKeyPath(String privateKeyPath) {
            this.privateKeyPath = privateKeyPath;
        }

        public String getMerchantSerialNo() {
            return merchantSerialNo;
        }

        public void setMerchantSerialNo(String merchantSerialNo) {
            this.merchantSerialNo = merchantSerialNo;
        }

        public String getApiV3Key() {
            return apiV3Key;
        }

        public void setApiV3Key(String apiV3Key) {
            this.apiV3Key = apiV3Key;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public String getRefundNotifyUrl() {
            return refundNotifyUrl;
        }

        public void setRefundNotifyUrl(String refundNotifyUrl) {
            this.refundNotifyUrl = refundNotifyUrl;
        }
    }

    public static class Wechat {
        private boolean mockEnabled = true;
        private String appId;
        private String appSecret;
        private String qrEnvVersion = "release";

        public boolean isMockEnabled() {
            return mockEnabled;
        }

        public void setMockEnabled(boolean mockEnabled) {
            this.mockEnabled = mockEnabled;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }

        public String getQrEnvVersion() {
            return qrEnvVersion;
        }

        public void setQrEnvVersion(String qrEnvVersion) {
            this.qrEnvVersion = qrEnvVersion;
        }
    }

    public static class Order {
        private boolean timeoutAutoCancelEnabled = true;
        private long timeoutCleanupDelayMs = 60000L;
        private boolean autoGoodReviewEnabled = true;
        private long autoGoodReviewDelayMs = 3600000L;

        public boolean isTimeoutAutoCancelEnabled() {
            return timeoutAutoCancelEnabled;
        }

        public void setTimeoutAutoCancelEnabled(boolean timeoutAutoCancelEnabled) {
            this.timeoutAutoCancelEnabled = timeoutAutoCancelEnabled;
        }

        public long getTimeoutCleanupDelayMs() {
            return timeoutCleanupDelayMs;
        }

        public void setTimeoutCleanupDelayMs(long timeoutCleanupDelayMs) {
            this.timeoutCleanupDelayMs = timeoutCleanupDelayMs;
        }

        public boolean isAutoGoodReviewEnabled() {
            return autoGoodReviewEnabled;
        }

        public void setAutoGoodReviewEnabled(boolean autoGoodReviewEnabled) {
            this.autoGoodReviewEnabled = autoGoodReviewEnabled;
        }

        public long getAutoGoodReviewDelayMs() {
            return autoGoodReviewDelayMs;
        }

        public void setAutoGoodReviewDelayMs(long autoGoodReviewDelayMs) {
            this.autoGoodReviewDelayMs = autoGoodReviewDelayMs;
        }
    }
}
