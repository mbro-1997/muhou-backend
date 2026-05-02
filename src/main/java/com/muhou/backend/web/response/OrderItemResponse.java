package com.muhou.backend.web.response;

public class OrderItemResponse extends PropResponse {

    private boolean outboundScanned;
    private boolean returnScanned;

    public boolean isOutboundScanned() { return outboundScanned; }
    public void setOutboundScanned(boolean outboundScanned) { this.outboundScanned = outboundScanned; }
    public boolean isReturnScanned() { return returnScanned; }
    public void setReturnScanned(boolean returnScanned) { this.returnScanned = returnScanned; }
}
