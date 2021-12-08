package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/games")
public class GameController {

    final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional
    @GetMapping()
    ResponseEntity<List<Game>> getGames(){
        var games = gameRepository.findAll();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("{title}")
    ResponseEntity<Game> getGame(@PathVariable String title){
        var game = gameRepository.findGameByTitle(title);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Game> addGame(@RequestBody Game newGame){
        gameRepository.save(newGame);

        return new ResponseEntity<>(newGame, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<Game> updateGame(@RequestBody Game updatedGame){
        gameRepository.save(updatedGame);

        return new ResponseEntity<>(updatedGame, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeGameFromSite(@RequestBody Game deleteGame){
        gameRepository.delete(deleteGame);

        return new ResponseEntity<>(deleteGame.getTitle() + " удалена из базы данных", HttpStatus.OK);
    }

}
