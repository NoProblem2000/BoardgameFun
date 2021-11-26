package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    ResponseEntity<List<Game>> getGames(){
        var games = gameRepository.findAll();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("{title}")
    ResponseEntity<Game> getGame(@PathVariable String title){
        var game = gameRepository.findGameByTitle(title);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
