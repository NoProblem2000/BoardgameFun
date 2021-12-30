package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryDTO;
import com.petproject.boardgamefun.dto.request.DiaryCommentRequest;
import com.petproject.boardgamefun.dto.request.DiaryRatingRequest;
import com.petproject.boardgamefun.model.DiaryComment;
import com.petproject.boardgamefun.model.DiaryRating;
import com.petproject.boardgamefun.repository.DiaryCommentRepository;
import com.petproject.boardgamefun.repository.DiaryRatingRepository;
import com.petproject.boardgamefun.repository.DiaryRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/diaries")
public class DiaryController {
    final DiaryCommentRepository diaryCommentRepository;
    final UserRepository userRepository;
    final DiaryRepository diaryRepository;
    final DiaryRatingRepository diaryRatingRepository;
    final DiaryService diaryService;

    public DiaryController(DiaryCommentRepository diaryCommentRepository, UserRepository userRepository, DiaryRepository diaryRepository, DiaryRatingRepository diaryRatingRepository, DiaryService diaryService) {
        this.diaryCommentRepository = diaryCommentRepository;
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
        this.diaryRatingRepository = diaryRatingRepository;
        this.diaryService = diaryService;
    }

    @Transactional
    @GetMapping("")
    public ResponseEntity<List<DiaryDTO>> getDiaries(){
        var diaries = diaryService.projectionsToDiaryDTO(diaryRepository.getAllDiaries());

        return new ResponseEntity<>(diaries, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDTO> getDiary(@PathVariable Integer diaryId){
        var diaryProjection = diaryService.projectionToDiaryDTO(diaryRepository.findDiaryUsingId(diaryId));

        return new ResponseEntity<>(diaryProjection, HttpStatus.OK);
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

    @Transactional
    @PostMapping("/{diaryId}/set-rating/{userId}")
    public ResponseEntity<DiaryRating> setDiaryRating(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody DiaryRatingRequest ratingRequest){
        var diary = diaryRepository.findDiaryById(diaryId);
        var user = userRepository.findUserById(userId);

        var diaryRating = new DiaryRating();
        diaryRating.setDiary(diary);
        diaryRating.setUser(user);
        diaryRating.setRating(ratingRequest.getRating());
        diaryRatingRepository.save(diaryRating);

        return new ResponseEntity<>(diaryRating, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/update-rating/{ratingId}")
    public ResponseEntity<DiaryRating> updateDiaryRating(@PathVariable Integer ratingId, @RequestBody DiaryRatingRequest ratingRequest){
        var diaryRating = diaryRatingRepository.findDiaryRatingById(ratingId);

        if (ratingRequest != null && !Objects.equals(diaryRating.getRating(), ratingRequest.getRating())){
            diaryRating.setRating(ratingRequest.getRating());
        }

        diaryRatingRepository.save(diaryRating);

        return new ResponseEntity<>(diaryRating, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-rating/{ratingId}")
    public ResponseEntity<String> deleteDiaryRating(@PathVariable Integer ratingId){
        var diaryRating = diaryRatingRepository.findDiaryRatingById(ratingId);
        diaryRatingRepository.delete(diaryRating);
        return new ResponseEntity<>("Рейтинг убран с игры", HttpStatus.OK);
    }


    //todo: add updated time to comment;
    //todo: add additional processing to ALL responses, not only OK
}
