package com.example.voting_app.vote.dto;

public class CastVoteRequest {
    private Integer optionId;
    private Integer userId;

    public Integer getOptionId() { return optionId; }
    public void setOptionId(Integer optionId) { this.optionId = optionId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
