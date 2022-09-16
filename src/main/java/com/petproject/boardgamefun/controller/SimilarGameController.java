package com.petproject.boardgamefun.controller;


import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.service.SimilarGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/similar-games")
public class SimilarGameController {

    final SimilarGameService similarGameService;

    public SimilarGameController(SimilarGameService service) {
        this.similarGameService = service;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<List<GameDataDTO>> getSimilarGames(@PathVariable Integer gameId) {

        var similarGames = similarGameService.getSimilarGames(gameId);
        return new ResponseEntity<>(similarGames, HttpStatus.OK);
    }

    @PostMapping("/{referenceGameId}/{sourceGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> addSimilarGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {

        var sameGames = similarGameService.addSimilarGame(referenceGameId, sourceGameId);
        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }

    @DeleteMapping("/{referenceGameId}/{sourceGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> deleteSameGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {

        var sameGames = similarGameService.deleteSameGame(referenceGameId, sourceGameId);
        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }
}
