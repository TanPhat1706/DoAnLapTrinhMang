package com.example.voting_app.voteresult;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteResultRepository extends JpaRepository<VoteResult, Integer> {
    long countByVote_VoteIdAndOption_OptionId(Integer voteId, Integer optionId);
    boolean existsByVote_VoteIdAndUser_UserId(Integer voteId, Integer userId);
    List<VoteResult> findByVote_VoteId(Integer voteId);
}
