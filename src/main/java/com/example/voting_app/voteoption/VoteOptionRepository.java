package com.example.voting_app.voteoption;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Integer> {
    List<VoteOption> findByVote_VoteId(Integer voteId);
}
