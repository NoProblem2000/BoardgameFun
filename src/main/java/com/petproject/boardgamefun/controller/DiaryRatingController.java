package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryRatingDTO;
import com.petproject.boardgamefun.dto.request.DiaryRatingRequest;
import com.petproject.boardgamefun.service.DiaryRatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/diary-rating")
public class DiaryRatingController {

    final DiaryRatingService diaryRatingService;

    public DiaryRatingController(DiaryRatingService diaryRatingService) {
        this.diaryRatingService = diaryRatingService;
    }

    @PostMapping("/{diaryId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryRatingDTO> setDiaryRating(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody DiaryRatingRequest ratingRequest) {
        var diaryRatingDTO = diaryRatingService.setDiaryRating(diaryId, userId, ratingRequest);
        return new ResponseEntity<>(diaryRatingDTO, HttpStatus.OK);
    }

    @PatchMapping("/{ratingId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryRatingDTO> updateDiaryRating(@PathVariable Integer ratingId, @RequestBody DiaryRatingRequest ratingRequest) {
        var diaryRatingDTO = diaryRatingService.updateDiaryRating(ratingId, ratingRequest);
        return new ResponseEntity<>(diaryRatingDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{ratingId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteDiaryRating(@PathVariable Integer ratingId) {
        diaryRatingService.deleteDiaryRating(ratingId);
        return new ResponseEntity<>("Рейтинг убран с игры", HttpStatus.OK);
    }
}
