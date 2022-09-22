package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.service.ExpansionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expansions")
public class ExpansionController {

    final ExpansionService expansionService;

    public ExpansionController(ExpansionService expansionService) {
        this.expansionService = expansionService;
    }

    @ApiOperation
            (value = "Get expansions by game id", notes = "Return games which have been expansions of current game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Expansions successfully retrieved"),
            @ApiResponse(code = 404, message = "Game with this id does not exist"),
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<List<GameDataDTO>> getExpansions(@PathVariable Integer gameId) {

        var gamesExpansions = expansionService.getExpansions(gameId);
        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add expansion to current game", notes = "Return games which have been expansions of current game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully added to expansions"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Daughter or parent game does not exist")
    })
    @PostMapping("/{parentGameId}/{daughterGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> addExpansion(@PathVariable Integer parentGameId, @PathVariable Integer daughterGameId) {

        var gamesExpansions = expansionService.addExpansion(parentGameId, daughterGameId);
        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete expansion from current game", notes = "Return games which have been expansions of current game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully deleted from expansions"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Expansion not found for parent")
    })
    @DeleteMapping("/{parentGameId}/{daughterGameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameDataDTO>> deleteExpansion(@PathVariable Integer parentGameId, @PathVariable Integer daughterGameId) {

        var gamesExpansions = expansionService.deleteExpansion(parentGameId, daughterGameId);
        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }
}
