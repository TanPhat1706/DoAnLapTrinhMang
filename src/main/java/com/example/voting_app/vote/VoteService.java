package com.example.voting_app.vote;

import com.example.voting_app.user.AppUser;
import com.example.voting_app.user.AppUserRepository;
import com.example.voting_app.voteoption.VoteOption;
import com.example.voting_app.voteoption.VoteOptionRepository;
import com.example.voting_app.voteresult.VoteResult;
import com.example.voting_app.voteresult.VoteResultRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoteOptionRepository optionRepository;
    private final VoteResultRepository resultRepository;
    private final AppUserRepository userRepository;

    public VoteService(VoteRepository voteRepository, VoteOptionRepository optionRepository,
                       VoteResultRepository resultRepository, AppUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Vote createVote(String title, LocalDateTime start, LocalDateTime end, Integer creatorUserId) {
        AppUser creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Creator user not found"));
        Vote v = new Vote();
        v.setTitle(title);
        v.setCreatedDate(LocalDate.now());
        v.setStartTime(start);
        v.setEndTime(end);
        v.setCreatedBy(creator);
        return voteRepository.save(v);
    }

    @Transactional
    public VoteOption addOption(Integer voteId, String text) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));
        VoteOption opt = new VoteOption();
        opt.setVote(vote);
        opt.setOptionText(text);
        return optionRepository.save(opt);
    }

    @Transactional
    public VoteResult castVote(Integer voteId, Integer optionId, Integer userId) {
        if (resultRepository.existsByVote_VoteIdAndUser_UserId(voteId, userId)) {
            throw new IllegalStateException("User already voted");
        }
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));
        VoteOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));
        if (!Objects.equals(option.getVote().getVoteId(), voteId)) {
            throw new IllegalArgumentException("Option doesn't belong to vote");
        }
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        VoteResult res = new VoteResult();
        res.setVote(vote);
        res.setOption(option);
        res.setUser(user);
        res.setVotedAt(LocalDateTime.now());
        return resultRepository.save(res);
    }

    public Map<Integer, Long> getResultCounts(Integer voteId) {
        Map<Integer, Long> counts = new LinkedHashMap<>();
        optionRepository.findByVote_VoteId(voteId).forEach(opt -> {
            long c = resultRepository.countByVote_VoteIdAndOption_OptionId(voteId, opt.getOptionId());
            counts.put(opt.getOptionId(), c);
        });
        return counts;
    }

    public List<VoteOption> listOptions(Integer voteId) {
        return optionRepository.findByVote_VoteId(voteId);
    }

    public byte[] generateQRCodeForVote(Integer voteId, String baseUrl) throws Exception {
        String link = baseUrl;
        if (!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";
        link = baseUrl + "vote/" + URLEncoder.encode(String.valueOf(voteId), StandardCharsets.UTF_8);
        BitMatrix matrix = new MultiFormatWriter().encode(link, BarcodeFormat.QR_CODE, 300, 300);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
            return baos.toByteArray();
        }
    }

    public byte[] exportResultsExcel(Integer voteId) throws Exception {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));
        List<VoteOption> options = optionRepository.findByVote_VoteId(voteId);
        Map<Integer, Long> counts = getResultCounts(voteId);

        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Results");
            int r = 0;
            Row header = sheet.createRow(r++);
            header.createCell(0).setCellValue("Vote Title");
            header.createCell(1).setCellValue(vote.getTitle());

            Row head2 = sheet.createRow(r++);
            head2.createCell(0).setCellValue("Option");
            head2.createCell(1).setCellValue("Count");

            for (VoteOption opt : options) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(opt.getOptionText());
                row.createCell(1).setCellValue(counts.getOrDefault(opt.getOptionId(), 0L));
            }
            wb.write(baos);
            return baos.toByteArray();
        }
    }

    public List<Vote> listVotes() {
        return voteRepository.findAll();
    }

    @Transactional
    public void deleteVote(Integer voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("Vote not found"));
        // Delete results first
        java.util.List<com.example.voting_app.voteresult.VoteResult> results = resultRepository.findByVote_VoteId(voteId);
        resultRepository.deleteAll(results);
        // Delete options
        java.util.List<com.example.voting_app.voteoption.VoteOption> options = optionRepository.findByVote_VoteId(voteId);
        optionRepository.deleteAll(options);
        // Delete vote
        voteRepository.delete(vote);
    }
}
