package com.example.voting_app.voteoption;

import com.example.voting_app.vote.Vote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "VoteOption")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OptionID")
    private Integer optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VoteID")
    private Vote vote;

    @Column(name = "OptionText", length = 200, nullable = false)
    private String optionText;

    public Integer getOptionId() { return optionId; }
    public void setOptionId(Integer optionId) { this.optionId = optionId; }
    public Vote getVote() { return vote; }
    public void setVote(Vote vote) { this.vote = vote; }
    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }
}
