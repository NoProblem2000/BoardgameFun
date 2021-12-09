package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.model.Expansion;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.repository.ExpansionRepository;
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
    final ExpansionRepository expansionRepository;

    public GameController(GameRepository gameRepository, ExpansionRepository expansionRepository) {
        this.gameRepository = gameRepository;
        this.expansionRepository = expansionRepository;
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

    @Transactional
    @GetMapping("/expansions/{gameId}")
    public ResponseEntity<List<Game>> getExpansions(@PathVariable Integer gameId){
        var gamesExpansions = gameRepository.getExpansions(gameId);

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add-expansion/{parentGameId}/{daughterGameId}")
    public ResponseEntity<List<Game>> addExpansion(@PathVariable Integer parentGameId, @PathVariable Integer daughterGameId){
        var parentGame = gameRepository.findGameById(parentGameId);
        var daughterGame = gameRepository.findGameById(daughterGameId);

        var expansion = new Expansion();
        expansion.setParentGame(parentGame);
        expansion.setDaughterGame(daughterGame);
        expansionRepository.save(expansion);

        var gamesExpansions = gameRepository.getExpansions(parentGameId);

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-expansion/{parentGameId}/{daughterGameId}")
    public ResponseEntity<List<Game>> deleteExpansion(@PathVariable Integer daughterGameId, @PathVariable Integer parentGameId){
        var expansion = expansionRepository.findExpansion_ByDaughterGameIdAndParentGameId(daughterGameId, parentGameId);
        expansionRepository.delete(expansion);

        var gamesExpansions = gameRepository.getExpansions(parentGameId);

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    //todo: add same game
    //todo: ratings list
    //todo: forum
    //todo: gat avg rating
}
