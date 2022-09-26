package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryRatingDTO;
import com.petproject.boardgamefun.dto.request.DiaryRatingRequest;
import com.petproject.boardgamefun.service.DiaryRatingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation
            (value = "Set the diary rating", notes = "Return new rating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary rating successfully set"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Diary or User not found")
    })
    @PostMapping("/{diaryId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryRatingDTO> setDiaryRating(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody DiaryRatingRequest ratingRequest) {
        var diaryRatingDTO = diaryRatingService.setDiaryRating(diaryId, userId, ratingRequest);
        return new ResponseEntity<>(diaryRatingDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update the diary rating", notes = "Return updated rating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary rating successfully updated"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Diary rating not found")
    })
    @PatchMapping("/{ratingId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryRatingDTO> updateDiaryRating(@PathVariable Integer ratingId, @RequestBody DiaryRatingRequest ratingRequest) {
        var diaryRatingDTO = diaryRatingService.updateDiaryRating(ratingId, ratingRequest);
        return new ResponseEntity<>(diaryRatingDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Remove the diary rating", notes = "Return status of deleted rating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary rating successfully removed"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Diary rating not found")
    })
    @DeleteMapping("/{ratingId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteDiaryRating(@PathVariable Integer ratingId) {
        diaryRatingService.deleteDiaryRating(ratingId);
        return new ResponseEntity<>("Рейтинг убран с игры", HttpStatus.OK);
    }
}
