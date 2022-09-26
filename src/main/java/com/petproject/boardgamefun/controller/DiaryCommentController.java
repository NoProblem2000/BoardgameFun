package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryCommentDTO;
import com.petproject.boardgamefun.dto.request.DiaryCommentRequest;
import com.petproject.boardgamefun.service.DiaryCommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation
            (value = "Get diary comments", notes = "Return comments list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary comments successfully retrieved")
    })
    @GetMapping("/{diaryId}")
    public ResponseEntity<List<DiaryCommentDTO>> getDiaryComments(@PathVariable Integer diaryId) {
        var diaryComments = diaryCommentService.getDiaryComments(diaryId);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add a comment to diary", notes = "Return new comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary comment successfully created"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Diary or User not found")
    })
    @PostMapping(value = "{diaryId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DiaryCommentDTO>> addComment(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody DiaryCommentRequest diaryCommentRequest) {
        var diaryComments = diaryCommentService.addDiaryComment(diaryId, userId, diaryCommentRequest);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update a diary comment", notes = "Return updated comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary comment successfully updated"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "No such comment found")
    })
    @PatchMapping(value = "{diaryId}/{diaryCommentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DiaryCommentDTO>> updateComment(@PathVariable Integer diaryId, @PathVariable Integer diaryCommentId, @RequestBody DiaryCommentRequest diaryCommentRequest) {
        var diaryComments = diaryCommentService.updateComment(diaryId, diaryCommentId, diaryCommentRequest);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete a diary comment", notes = "Return status of deleted comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary comment successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "No such comment found")
    })
    @DeleteMapping("{diaryId}/{diaryCommentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DiaryCommentDTO>> deleteComment(@PathVariable Integer diaryId, @PathVariable Integer diaryCommentId) {
        var diaryComments = diaryCommentService.deleteComment(diaryId, diaryCommentId);
        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }
}
