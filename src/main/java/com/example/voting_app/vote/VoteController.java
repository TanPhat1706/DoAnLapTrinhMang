package com.example.voting_app.vote;

import com.example.voting_app.voteoption.VoteOption;
import com.example.voting_app.vote.dto.CreateVoteRequest;
import com.example.voting_app.vote.dto.AddOptionRequest;
import com.example.voting_app.vote.dto.CastVoteRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    // Tạo vote (ALL)
    @PostMapping
    public Vote create(@RequestParam String title,
                       @RequestParam(required = false) String start,
                       @RequestParam(required = false) String end,
                       @RequestParam Integer createdBy) {
        LocalDateTime s = start != null ? LocalDateTime.parse(start) : null;
        LocalDateTime e = end != null ? LocalDateTime.parse(end) : null;
        return voteService.createVote(title, s, e, createdBy);
    }

    // Tạo vote bằng JSON body (dễ dùng trên Postman)
    @PostMapping(path = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Vote createJson(@RequestBody CreateVoteRequest req) {
        LocalDateTime s = (req.getStart() != null && !req.getStart().isEmpty()) ? LocalDateTime.parse(req.getStart()) : null;
        LocalDateTime e = (req.getEnd() != null && !req.getEnd().isEmpty()) ? LocalDateTime.parse(req.getEnd()) : null;
        return voteService.createVote(req.getTitle(), s, e, req.getCreatedBy());
    }

    // Thêm lựa chọn
    @PostMapping("/{voteId}/options")
    public VoteOption addOption(@PathVariable Integer voteId, @RequestParam String text) {
        return voteService.addOption(voteId, text);
    }

    // Thêm lựa chọn bằng JSON body
    @PostMapping(path = "/{voteId}/options/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteOption addOptionJson(@PathVariable Integer voteId, @RequestBody AddOptionRequest req) {
        return voteService.addOption(voteId, req.getText());
    }

    // Danh sách lựa chọn của vote
    @GetMapping("/{voteId}/options")
    public java.util.List<VoteOption> listOptions(@PathVariable Integer voteId) {
        return voteService.listOptions(voteId);
    }

    // Danh sách các vote
    @GetMapping
    public java.util.List<Vote> listVotes() {
        return voteService.listVotes();
    }

    // Update lượt vote (bình chọn)
    @PostMapping("/{voteId}/cast")
    public ResponseEntity<?> cast(@PathVariable Integer voteId,
                                  @RequestParam Integer optionId,
                                  @RequestParam Integer userId) {
        return ResponseEntity.ok(voteService.castVote(voteId, optionId, userId));
    }

    // Update lượt vote (bình chọn) bằng JSON body
    @PostMapping(path = "/{voteId}/cast/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> castJson(@PathVariable Integer voteId, @RequestBody CastVoteRequest req) {
        return ResponseEntity.ok(voteService.castVote(voteId, req.getOptionId(), req.getUserId()));
    }

    // Xem lượt vote
    @GetMapping("/{voteId}/results")
    public Map<Integer, Long> results(@PathVariable Integer voteId) {
        return voteService.getResultCounts(voteId);
    }

    // Sinh QR code / links
    @GetMapping(value = "/{voteId}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qr(@PathVariable Integer voteId,
                                     @RequestParam(defaultValue = "http://localhost:8080") String baseUrl) throws Exception {
        byte[] png = voteService.generateQRCodeForVote(voteId, baseUrl);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=vote-" + voteId + ".png")
                .body(png);
    }

    // Xuất kết quả (Excel)
    @GetMapping(value = "/{voteId}/export", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> export(@PathVariable Integer voteId) throws Exception {
        byte[] xlsx = voteService.exportResultsExcel(voteId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vote-" + voteId + ".xlsx")
                .body(xlsx);
    }

    // Xoá vote (cùng tất cả kết quả và lựa chọn)
    @DeleteMapping("/{voteId}")
    public ResponseEntity<Void> delete(@PathVariable Integer voteId) {
        voteService.deleteVote(voteId);
        return ResponseEntity.noContent().build();
    }
}
