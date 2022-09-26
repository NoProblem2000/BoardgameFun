package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryDataDTO;
import com.petproject.boardgamefun.model.Diary;
import com.petproject.boardgamefun.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/diaries")
public class DiaryController {
    final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping("")
    public ResponseEntity<List<DiaryDataDTO>> getDiaries(@RequestParam(required = false) Integer userId, @RequestParam(required = false) Integer gameId) {
        var diaries = diaryService.getDiaries(userId, gameId);
        return new ResponseEntity<>(diaries, HttpStatus.OK);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDataDTO> getDiaryById(@PathVariable Integer diaryId) {
        var diaryProjection = diaryService.getDiaryById(diaryId);
        return new ResponseEntity<>(diaryProjection, HttpStatus.OK);
    }

    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryDataDTO> addDiary(@PathVariable Integer userId, @PathVariable Integer gameId, @RequestBody Diary diary) {
        var diaryDTO = diaryService.addDiary(userId, gameId, diary);
        return new ResponseEntity<>(diaryDTO, HttpStatus.OK);
    }

    @DeleteMapping("{userId/{diaryId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteDiary(@PathVariable Integer userId, @PathVariable Integer diaryId) {
        String title = diaryService.deleteDiary(userId, diaryId);
        return new ResponseEntity<>("Дневник " + title + " удален из ваших дневников", HttpStatus.OK);
    }

    @PutMapping({"{userId}/{diaryId}"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryDataDTO> updateDiary(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody Diary diaryRequest) {
        var diaryDTO = diaryService.updateDiary(diaryId, userId, diaryRequest);
        return new ResponseEntity<>(diaryDTO, HttpStatus.OK);
    }


    //todo: add updated time to comment;
    //todo: add additional processing to ALL responses, not only OK
}
