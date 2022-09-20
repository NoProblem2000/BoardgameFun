package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.model.GameSell;
import com.petproject.boardgamefun.service.GameSellService;
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

    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameSellDTO>> addGameToSellList(@PathVariable Integer userId, @PathVariable Integer gameId, @RequestBody GameSell gameSell) {
        List<GameSellDTO> gameSellList = gameSellService.addGameToSellList(userId, gameId, gameSell);
        return new ResponseEntity<>(gameSellList, HttpStatus.OK);
    }

    @DeleteMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> removeGameFromSell(@PathVariable Integer userId, @PathVariable Integer gameId) {
        gameSellService.removeGameFromSell(userId, gameId);
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @PutMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> updateSellGame(@RequestBody GameSell gameSell) {
        gameSellService.updateGameSell(gameSell);
        return new ResponseEntity<>(gameSell.getGame().getTitle() + " обновлена", HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<GameSellDTO>> getGameSellList(@PathVariable Integer userId) {

        var gameSellList = gameSellService.getGameSellList(userId);
        return new ResponseEntity<>(gameSellList, HttpStatus.OK);
    }
}
