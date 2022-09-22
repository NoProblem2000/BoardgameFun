package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.FilterGamesDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.service.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation
            (value = "Get games", notes = "Return all games")
    @ApiResponse(code = 200, message = "Games successfully retrieved")
    @GetMapping()
    ResponseEntity<List<GameDataDTO>> getGames() {
        var games = gameService.getGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get game by id", notes = "Return game by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game with this id successfully retrieved"),
            @ApiResponse(code = 404, message = "Game with this id not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<GameDataDTO> getGameById(@PathVariable Integer id) {
        var gameDataDTO = gameService.getGameById(id);
        return new ResponseEntity<>(gameDataDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get game by title", notes = "Return games by title matches")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Games with matches title successfully retrieved"),
            @ApiResponse(code = 404, message = "Games with this title not matches"),
    })
    @GetMapping("/get-games-by-filter/{title}")
    public ResponseEntity<List<FilterGamesDTO>> getGamesByTitle(@PathVariable String title) {
        var games = gameService.getGameByTitle(title);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add games", notes = "Return created game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully created"),
            @ApiResponse(code = 400, message = "Game model has bad fields values"),
            @ApiResponse(code = 400, message = "Game already exist"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 403, message = "You does not have such permissions")
    })
    @PostMapping("/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> addGame(@RequestBody Game newGame) {
        gameService.checkExistence(newGame);
        return getGameDataDTOResponseEntity(newGame);
    }

    @ApiOperation
            (value = "Upload game image", notes = "Return updated game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Image successfully uploaded"),
            @ApiResponse(code = 404, message = "Game with this id does not exist"),
            @ApiResponse(code = 401, message = "You are not authorized")
    })
    @PostMapping("/upload-image/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<GameDataDTO> uploadImage(@PathVariable Integer gameId, @RequestParam("picture") MultipartFile file) throws IOException {
        var gameDTO = gameService.uploadImage(gameId, file);
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update the game data", notes = "Return updated game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully updated"),
            @ApiResponse(code = 400, message = "Game model has bad fields values"),
            @ApiResponse(code = 400, message = "Game already exist"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 403, message = "You does not have such permissions")
    })
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

    @ApiOperation
            (value = "Delete the game", notes = "Return status of deleted game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully deleted"),
            @ApiResponse(code = 404, message = "Game doesn't exist"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 403, message = "You does not have such permissions")
    })
    @DeleteMapping("/")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> removeGameFromSite(@RequestBody Game deleteGame) {
        gameService.delete(deleteGame);
        return new ResponseEntity<>(deleteGame.getTitle() + " удалена из базы данных", HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete the game", notes = "Return status of deleted game")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Games by user successfully retrieved")
    })
    @GetMapping("/{userId}/games")
    public ResponseEntity<List<GameDataDTO>> getUserCollection(@PathVariable Integer userId) {

        var games = gameService.getUserCollection(userId);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    // todo: use standard hibernate deleteById and findById!!!
    //todo: ratings list
}
