package com.stayready.poll_application.controller;

import com.stayready.poll_application.domain.Vote;
import com.stayready.poll_application.repositories.VoteRepository;
import dtos.OptionCount;
import dtos.VoteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ComputeResultController {

    private VoteRepository voteRepository;

    @Autowired
    public ComputeResultController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);

        //TODO: Implement algorithm to count votes
        int total = 0;
        Map<Long, OptionCount> voteMap = new HashMap<>();
        for (Vote vote : allVotes) {
            total++;
            OptionCount optionCount = voteMap.get(vote.getOption().getId());
            if (optionCount == null) {
                optionCount = new OptionCount();
                optionCount.setOptionId(vote.getId());
                voteMap.put(vote.getOption().getId(), optionCount);
            }
            optionCount.setCount(optionCount.getCount() + 1);
        }
        voteResult.setTotalVotes(total);
        voteResult.setResults(voteMap.values());
        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }}
