package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.FilterGamesDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.service.GameService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    final GameService gameService;

    public GameController(GameService gameService) {
       this.gameService = gameService;
    }

    @GetMapping()
    ResponseEntity<List<GameDataDTO>> getGames() {
        var games = gameService.getGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDataDTO> getGameById(@PathVariable Integer id) {
        var gameDataDTO = gameService.getGameById(id);
        return new ResponseEntity<>(gameDataDTO, HttpStatus.OK);
    }

    @GetMapping("/get-games-by-filter/{title}")
    public ResponseEntity<List<FilterGamesDTO>> getGamesByTitle(@PathVariable String title) {
        var games = gameService.getGameByTitle(title);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> addGame(@RequestBody Game newGame) {
        gameService.checkExistence(newGame);
        return getGameDataDTOResponseEntity(newGame);
    }

    @PostMapping("/upload-image/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> uploadImage(@PathVariable Integer gameId, @RequestParam("picture") MultipartFile file) throws IOException {
        var gameDTO = gameService.uploadImage(gameId, file);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> updateGame(@RequestBody Game updatedGame) {
        return getGameDataDTOResponseEntity(updatedGame);
    }

    @NotNull
    private ResponseEntity<GameDataDTO> getGameDataDTOResponseEntity(Game updatedGame) {
        var game = gameService.save(updatedGame);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> removeGameFromSite(@RequestBody Game deleteGame) {
        gameService.delete(deleteGame);
        return new ResponseEntity<>(deleteGame.getTitle() + " удалена из базы данных", HttpStatus.OK);
    }
    @GetMapping("/{userId}/games")
    public ResponseEntity<List<GameDataDTO>> getUserCollection(@PathVariable Integer userId) {

        var games = gameService.getUserCollection(userId);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    // todo: use standard hibernate deleteById and findById!!!
    //todo: ratings list
}
