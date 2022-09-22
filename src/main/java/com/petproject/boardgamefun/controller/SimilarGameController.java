package com.petproject.boardgamefun.controller;


import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.service.SimilarGameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation
            (value = "Get similar games", notes = "Return similar games to current game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Similar games successfully retrieved"),
            @ApiResponse(code = 404, message = "Game with id not found")
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<List<GameDataDTO>> getSimilarGames(@PathVariable Integer gameId) {

        var similarGames = similarGameService.getSimilarGames(gameId);
        return new ResponseEntity<>(similarGames, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add similar game", notes = "Return similar games to current game after adding new game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Similar game successfully added"),
            @ApiResponse(code = 401, message = "You are not authorized to add similar game"),
            @ApiResponse(code = 404, message = "Reference or so source game with this id does not exist")
    })
    @PostMapping("/{referenceGameId}/{sourceGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> addSimilarGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {

        var sameGames = similarGameService.addSimilarGame(referenceGameId, sourceGameId);
        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete similar game", notes = "Return similar games to current game after delete game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Similar game successfully added"),
            @ApiResponse(code = 401, message = "You are not authorized to delete similar game"),
            @ApiResponse(code = 404, message = "No similar game found")
    })
    @DeleteMapping("/{referenceGameId}/{sourceGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> deleteSameGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {

        var sameGames = similarGameService.deleteSameGame(referenceGameId, sourceGameId);
        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }
}
