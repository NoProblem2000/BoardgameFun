package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryCommentRequest;
import com.petproject.boardgamefun.model.DiaryComment;
import com.petproject.boardgamefun.model.DiaryRating;
import com.petproject.boardgamefun.repository.DiaryCommentRepository;
import com.petproject.boardgamefun.repository.DiaryRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/diaries")
public class DiaryController {
    final DiaryCommentRepository diaryCommentRepository;
    final UserRepository userRepository;
    final DiaryRepository diaryRepository;

    public DiaryController(DiaryCommentRepository diaryCommentRepository, UserRepository userRepository, DiaryRepository diaryRepository) {
        this.diaryCommentRepository = diaryCommentRepository;
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
    }

    @Transactional
    @GetMapping("/{diaryId}/comments")
    public ResponseEntity<List<DiaryComment>> getDiaryComments(@PathVariable Integer diaryId){
        var diaryComments = diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId);

        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value = "{diaryId}/add-comment/{userId}")
    public ResponseEntity<List<DiaryComment>> addComment(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody DiaryCommentRequest diaryCommentRequest){

        var user = userRepository.findUserById(userId);
        var diary = diaryRepository.findDiaryById(diaryId);

        var diaryComment = new DiaryComment();
        diaryComment.setDiary(diary);
        diaryComment.setUser(user);
        diaryComment.setCommentTime(OffsetDateTime.now());
        diaryComment.setComment(diaryCommentRequest.getComment());
        diaryCommentRepository.save(diaryComment);

        var diaryComments = diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId);

        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping(value = "{diaryId}/update-comment/{diaryCommentId}")
    public ResponseEntity<List<DiaryComment>> updateComment(@PathVariable Integer diaryId, @PathVariable Integer diaryCommentId, @RequestBody DiaryCommentRequest diaryCommentRequest){
        var diaryComment = diaryCommentRepository.findDiaryCommentById(diaryCommentId);
        if (diaryCommentRequest != null && !diaryCommentRequest.getComment().equals(diaryComment.getComment())) {
            diaryComment.setComment(diaryCommentRequest.getComment());
            diaryCommentRepository.save(diaryComment);
        }

        var diaryComments = diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId);

        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{diaryId}/delete-comment/{diaryCommentId}")
    public ResponseEntity<List<DiaryComment>> deleteComment(@PathVariable Integer diaryId, @PathVariable Integer diaryCommentId){

        var diaryComment = diaryCommentRepository.findDiaryCommentById(diaryCommentId);
        diaryCommentRepository.delete(diaryComment);
        var diaryComments = diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId);

        return new ResponseEntity<>(diaryComments, HttpStatus.OK);
    }

    @PostMapping("/{diaryId}/set-rating/{userId}")
    public ResponseEntity<DiaryRating> setDiaryRating(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody Integer rating){
        return null;
    }

    @PutMapping("/{diaryId}/update-rating/{userId}")
    public ResponseEntity<DiaryRating> updateDiaryRating(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody Integer rating){
        return null;
    }

    @DeleteMapping("/{diaryId}/delete-rating/{userId}")
    public ResponseEntity<String> deleteDiaryRating(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody Integer rating){
        return null;
    }


    //todo: add updated time to comment;
    //todo: add additional processing to ALL responses, not only OK
}
