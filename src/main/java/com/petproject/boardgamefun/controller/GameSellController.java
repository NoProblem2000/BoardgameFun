package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.model.GameSell;
import com.petproject.boardgamefun.service.GameSellService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game-sell")
public class GameSellController {
    final GameSellService gameSellService;

    public GameSellController(GameSellService gameSellService) {
        this.gameSellService = gameSellService;
    }

    @ApiOperation
            (value = "Add game to user sell list", notes = "Return games from sell list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully added to sell list"),
            @ApiResponse(code = 400, message = "Game for sell already exists"),
            @ApiResponse(code = 401, message = "You are not authorized to add game to sell list"),
            @ApiResponse(code = 404, message = "User or game does not exist")
    })
    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameSellDTO>> addGameToSellList(@PathVariable Integer userId, @PathVariable Integer gameId, @RequestBody GameSell gameSell) {
        List<GameSellDTO> gameSellList = gameSellService.addGameToSellList(userId, gameId, gameSell);
        return new ResponseEntity<>(gameSellList, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete user game from sell list", notes = "Return games from sell list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully deleted from sell list"),
            @ApiResponse(code = 401, message = "You are not authorized to delete game from sell list"),
            @ApiResponse(code = 404, message = "No game for sell is found")
    })
    @DeleteMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> removeGameFromSell(@PathVariable Integer userId, @PathVariable Integer gameId) {
        gameSellService.removeGameFromSell(userId, gameId);
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update user game data for sell", notes = "Return status of updated game from sell list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game data successfully updated for sell list"),
            @ApiResponse(code = 400, message = "Nothing to update"),
            @ApiResponse(code = 401, message = "You are not authorized to update game data for sell list"),
            @ApiResponse(code = 404, message = "No game for sell is found")
    })
    @PutMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> updateSellGame(@RequestBody GameSell gameSell) {
        gameSellService.updateGameSell(gameSell);
        return new ResponseEntity<>(gameSell.getGame().getTitle() + " обновлена", HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get all user games from sell list", notes = "Return user games from sell list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sell list successfully retrieved"),
            @ApiResponse(code = 404, message = "User with id not found")
    })
    @GetMapping("{userId}")
    public ResponseEntity<List<GameSellDTO>> getGameSellList(@PathVariable Integer userId) {

        var gameSellList = gameSellService.getGameSellList(userId);
        return new ResponseEntity<>(gameSellList, HttpStatus.OK);
    }
}
