package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryDataDTO;
import com.petproject.boardgamefun.model.Diary;
import com.petproject.boardgamefun.service.DiaryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/diaries")
public class DiaryController {
    final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @ApiOperation
            (value = "Get diaries", notes = "Return diaries")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diaries successfully retrieved")
    })
    @GetMapping("")
    public ResponseEntity<List<DiaryDataDTO>> getDiaries(@RequestParam(required = false) Integer userId, @RequestParam(required = false) Integer gameId) {
        var diaries = diaryService.getDiaries(userId, gameId);
        return new ResponseEntity<>(diaries, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get diary by id", notes = "Return diary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diaries successfully retrieved")
    })
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDataDTO> getDiaryById(@PathVariable Integer diaryId) {
        var diaryProjection = diaryService.getDiaryById(diaryId);
        return new ResponseEntity<>(diaryProjection, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add the diary", notes = "Return new diary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary successfully created"),
            @ApiResponse(code = 400, message = "Bad diary model"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Game or User not found")
    })
    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryDataDTO> addDiary(@PathVariable Integer userId, @PathVariable Integer gameId, @RequestBody Diary diary) {
        var diaryDTO = diaryService.addDiary(userId, gameId, diary);
        return new ResponseEntity<>(diaryDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete the diary", notes = "Return status of deleted diary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Diary not found")
    })
    @DeleteMapping("{userId}/{diaryId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteDiary(@PathVariable Integer userId, @PathVariable Integer diaryId) {
        String title = diaryService.deleteDiary(userId, diaryId);
        return new ResponseEntity<>("Дневник " + title + " удален из ваших дневников", HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update the diary data", notes = "Return updated diary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Diary successfully updated"),
            @ApiResponse(code = 400, message = "Bad diary model"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Diary not found")
    })
    @PutMapping({"{userId}/{diaryId}"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryDataDTO> updateDiary(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody Diary diaryRequest) {
        var diaryDTO = diaryService.updateDiary(diaryId, userId, diaryRequest);
        return new ResponseEntity<>(diaryDTO, HttpStatus.OK);
    }


    //todo: add updated time to comment;
    //todo: add additional processing to ALL responses, not only OK
}
