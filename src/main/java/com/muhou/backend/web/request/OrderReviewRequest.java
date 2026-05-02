package com.muhou.backend.web.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderReviewRequest {

    @Min(value = 1, message = "score must be at least 1")
    @Max(value = 5, message = "score must be at most 5")
    private Integer score;

    @Min(value = 1, message = "propScore must be at least 1")
    @Max(value = 5, message = "propScore must be at most 5")
    private Integer propScore;

    @Min(value = 1, message = "counterpartyScore must be at least 1")
    @Max(value = 5, message = "counterpartyScore must be at most 5")
    private Integer counterpartyScore;

    private String content;

    @NotBlank(message = "reviewerRole is required")
    private String reviewerRole;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getPropScore() {
        return propScore;
    }

    public void setPropScore(Integer propScore) {
        this.propScore = propScore;
    }

    public Integer getCounterpartyScore() {
        return counterpartyScore;
    }

    public void setCounterpartyScore(Integer counterpartyScore) {
        this.counterpartyScore = counterpartyScore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewerRole() {
        return reviewerRole;
    }

    public void setReviewerRole(String reviewerRole) {
        this.reviewerRole = reviewerRole;
    }
}
