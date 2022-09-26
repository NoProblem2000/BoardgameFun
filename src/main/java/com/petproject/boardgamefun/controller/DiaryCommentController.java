package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryCommentDTO;
import com.petproject.boardgamefun.dto.request.DiaryCommentRequest;
import com.petproject.boardgamefun.service.DiaryCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diary-comments")
public class DiaryCommentController {
    final DiaryCommentService diaryCommentService;

    public DiaryCommentController(DiaryCommentService diaryCommentService) {
        this.diaryCommentService = diaryCommentService;
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<List<DiaryCommentDTO>> getDiaryComments(@PathVariable Integer diaryId) {
        var diaryComments = diaryCommentService.getDiaryComments(diaryId);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @PostMapping(value = "{diaryId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DiaryCommentDTO>> addComment(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody DiaryCommentRequest diaryCommentRequest) {
        var diaryComments = diaryCommentService.addDiaryComment(diaryId, userId, diaryCommentRequest);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @PatchMapping(value = "{diaryId}/{diaryCommentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DiaryCommentDTO>> updateComment(@PathVariable Integer diaryId, @PathVariable Integer diaryCommentId, @RequestBody DiaryCommentRequest diaryCommentRequest) {
        var diaryComments = diaryCommentService.updateComment(diaryId, diaryCommentId, diaryCommentRequest);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @DeleteMapping("{diaryId}/{diaryCommentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DiaryCommentDTO>> deleteComment(@PathVariable Integer diaryId, @PathVariable Integer diaryCommentId) {
        var diaryComments = diaryCommentService.deleteComment(diaryId, diaryCommentId);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }
}
