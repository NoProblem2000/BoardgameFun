package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.RatingGameByUserDTO;
import com.petproject.boardgamefun.service.RatingGameByUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rating-game-by-user")
public class RatingGameByUserController {
    final RatingGameByUserService ratingGameByUserService;

    public RatingGameByUserController(RatingGameByUserService ratingGameByUserService) {
        this.ratingGameByUserService = ratingGameByUserService;
    }

    @ApiOperation
            (value = "Delete user rating from game", notes = "Return status of deleting rating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rating successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized to delete rating"),
            @ApiResponse(code = 404, message = "Rating does not exist")
    })
    @DeleteMapping("/{userId}/delete-game-rating/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteGameRating(@PathVariable Integer userId, @PathVariable Integer gameId) {
        ratingGameByUserService.deleteGameRating(userId, gameId);
        return new ResponseEntity<>("Оценка с текущей игры удалена", HttpStatus.OK);
    }

    @ApiOperation
            (value = "Set game rating by user", notes = "Return game rating by user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rating successfully set"),
            @ApiResponse(code = 400, message = "Incorrect rating value or Rating already exists"),
            @ApiResponse(code = 401, message = "You are not authorized to set rating"),
            @ApiResponse(code = 404, message = "User or game does not exist")
    })
    @PostMapping("/{userId}/{gameId}/{rating}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Double> setGameRating(@PathVariable Integer userId, @PathVariable Integer gameId, @PathVariable Integer rating) {
        Double res = ratingGameByUserService.setGameRating(userId, gameId, rating);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update game rating by user", notes = "Return game rating by user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rating successfully updated"),
            @ApiResponse(code = 400, message = "Incorrect rating value"),
            @ApiResponse(code = 401, message = "You are not authorized to update rating"),
            @ApiResponse(code = 404, message = "Rating does not exist")
    })
    @PatchMapping("/{userId}/{gameId}/{rating}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Double> updateGameRating(@PathVariable Integer userId, @PathVariable Integer gameId, @PathVariable Integer rating) {
        Double res = ratingGameByUserService.updateGameRating(userId, gameId, rating);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get game rating by users", notes = "Return game ratings by users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ratings successfully retrieved"),
    })
    @GetMapping("/{gameId}/users-rating")
    public ResponseEntity<List<RatingGameByUserDTO>> getUsersRating(@PathVariable Integer gameId) {

        var ratings = ratingGameByUserService.getUsersRating(gameId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get games rating by user", notes = "Return games rating by user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ratings successfully retrieved"),
    })
    @GetMapping("/{userId}/games-rating")
    public ResponseEntity<List<GameDataDTO>> getUserRatingList(@PathVariable Integer userId) {

        var ratingGamesByUser = ratingGameByUserService.getUserRatingList(userId);
        return new ResponseEntity<>(ratingGamesByUser, HttpStatus.OK);
    }

}
