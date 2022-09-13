package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.service.RatingGameByUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating-game-by-user")
public class RatingGameByUserController {
    final RatingGameByUserService ratingGameByUserService;

    public RatingGameByUserController(RatingGameByUserService ratingGameByUserService) {
        this.ratingGameByUserService = ratingGameByUserService;
    }

    @DeleteMapping("/{userId}/delete-game-rating/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteGameRating(@PathVariable Integer userId, @PathVariable Integer gameId) {
        ratingGameByUserService.deleteGameRating(userId, gameId);
        return new ResponseEntity<>("Оценка с текущей игры удалена", HttpStatus.OK);
    }

    @PostMapping("/{userId}/{gameId}/{rating}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Double> setGameRating(@PathVariable Integer userId, @PathVariable Integer gameId, @PathVariable Integer rating) {
        Double res = ratingGameByUserService.setGameRating(userId, gameId, rating);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("/{userId}/{gameId}/{rating}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Double> updateGameRating(@PathVariable Integer userId, @PathVariable Integer gameId, @PathVariable Integer rating) {
        Double res = ratingGameByUserService.updateGameRating(userId, gameId, rating);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
