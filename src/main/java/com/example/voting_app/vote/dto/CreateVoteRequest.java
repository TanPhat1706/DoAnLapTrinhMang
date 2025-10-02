package com.example.voting_app.vote.dto;

public class CreateVoteRequest {
    private String title;
    private String start; // ISO-8601 date-time, e.g., 2025-10-02T09:00:00
    private String end;   // ISO-8601 date-time
    private Integer createdBy;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }
    public String getEnd() { return end; }
    public void setEnd(String end) { this.end = end; }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
}
