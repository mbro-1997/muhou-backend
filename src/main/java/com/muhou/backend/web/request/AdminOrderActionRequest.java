package com.muhou.backend.web.request;

import java.math.BigDecimal;

public class AdminOrderActionRequest {
    private String actionType;
    private BigDecimal amount;
    private String reason;
    private String internalNote;

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getInternalNote() { return internalNote; }
    public void setInternalNote(String internalNote) { this.internalNote = internalNote; }
}
