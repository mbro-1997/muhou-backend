package com.muhou.backend.web.response;

public class OrderItemResponse extends PropResponse {

    private boolean outboundScanned;
    private boolean returnScanned;
    private Integer outboundScannedCount;
    private Integer returnScannedCount;

    public boolean isOutboundScanned() { return outboundScanned; }
    public void setOutboundScanned(boolean outboundScanned) { this.outboundScanned = outboundScanned; }
    public boolean isReturnScanned() { return returnScanned; }
    public void setReturnScanned(boolean returnScanned) { this.returnScanned = returnScanned; }
    public Integer getOutboundScannedCount() { return outboundScannedCount; }
    public void setOutboundScannedCount(Integer outboundScannedCount) { this.outboundScannedCount = outboundScannedCount; }
    public Integer getReturnScannedCount() { return returnScannedCount; }
    public void setReturnScannedCount(Integer returnScannedCount) { this.returnScannedCount = returnScannedCount; }
}
