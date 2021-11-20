package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.repository.GameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
