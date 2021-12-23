package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.projection.GameProjection;
import com.petproject.boardgamefun.dto.projection.UsersGameRatingProjection;
import com.petproject.boardgamefun.model.Expansion;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.model.GameByDesigner;
import com.petproject.boardgamefun.model.SameGame;
import com.petproject.boardgamefun.repository.*;
import com.petproject.boardgamefun.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    final GameRepository gameRepository;
    final UserRepository userRepository;
    final ExpansionRepository expansionRepository;
    final SameGameRepository sameGameRepository;
    final DesignerRepository designerRepository;
    final GameByDesignerRepository gameByDesignerRepository;
    final GameService gameService;

    public GameController(GameRepository gameRepository, UserRepository userRepository, ExpansionRepository expansionRepository, SameGameRepository sameGameRepository, DesignerRepository designerRepository, GameByDesignerRepository gameByDesignerRepository, GameService gameService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.expansionRepository = expansionRepository;
        this.sameGameRepository = sameGameRepository;
        this.designerRepository = designerRepository;
        this.gameByDesignerRepository = gameByDesignerRepository;
        this.gameService = gameService;
    }

    @Transactional
    @GetMapping()
    ResponseEntity<List<GameProjection>> getGames() {

        var games = gameRepository.findGames();

        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/get-game/{id}")
    public ResponseEntity<GameDTO> getGameByCriteria(@PathVariable Integer id) {
        var gameDTO = gameService.projectionsToGameDTO(gameRepository.findGame(id), designerRepository.findDesignersUsingGame(id));
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/get-games-by-filter/{title}")
    public ResponseEntity<List<String>> getGamesByTitle(@PathVariable String title){
        var games = gameService.getTitlesFromProjections(gameRepository.findGamesUsingTitle(title));
        return new ResponseEntity<>(games, HttpStatus.OK);
    }


    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Game> addGame(@RequestBody Game newGame) {
        gameRepository.save(newGame);

        return new ResponseEntity<>(newGame, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<Game> updateGame(@RequestBody Game updatedGame) {
        gameRepository.save(updatedGame);

        return new ResponseEntity<>(updatedGame, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeGameFromSite(@RequestBody Game deleteGame) {
        gameRepository.delete(deleteGame);

        return new ResponseEntity<>(deleteGame.getTitle() + " удалена из базы данных", HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/expansions/{gameId}")
    public ResponseEntity<List<Game>> getExpansions(@PathVariable Integer gameId) {
        var gamesExpansions = gameRepository.getExpansions(gameId);

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add-expansion/{parentGameId}/{daughterGameId}")
    public ResponseEntity<List<Game>> addExpansion(@PathVariable Integer parentGameId, @PathVariable Integer daughterGameId) {
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
    public ResponseEntity<List<Game>> deleteExpansion(@PathVariable Integer daughterGameId, @PathVariable Integer parentGameId) {
        var expansion = expansionRepository.findExpansion_ByDaughterGameIdAndParentGameId(daughterGameId, parentGameId);
        expansionRepository.delete(expansion);

        var gamesExpansions = gameRepository.getExpansions(parentGameId);

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/similar/{gameId}")
    public ResponseEntity<List<Game>> getSimilarGames(@PathVariable Integer gameId) {
        var similarGames = gameRepository.getSimilarGames(gameId);

        return new ResponseEntity<>(similarGames, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add-similar/{referenceGameId}/{sourceGameId}")
    public ResponseEntity<List<Game>> addSimilarGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {
        var referenceGame = gameRepository.findGameById(referenceGameId);
        var sourceGame = gameRepository.findGameById(sourceGameId);

        var sameGame = new SameGame();
        sameGame.setReferenceGame(referenceGame);
        sameGame.setSourceGame(sourceGame);
        sameGameRepository.save(sameGame);

        var sameGames = gameRepository.getSimilarGames(referenceGameId);

        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-similar/{referenceGameId}/{sourceGameId}")
    public ResponseEntity<List<Game>> deleteSameGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {
        var sameGame = sameGameRepository.findSameGame_ByReferenceGameIdAndSourceGameId(referenceGameId, sourceGameId);
        sameGameRepository.delete(sameGame);

        var sameGames = gameRepository.getSimilarGames(referenceGameId);

        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{gameId}/users-rating")
    public ResponseEntity<List<UsersGameRatingProjection>> getUsersRating(@PathVariable Integer gameId) {

        var ratings = userRepository.findGameRatings(gameId);

        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/with-rating")
    public ResponseEntity<List<GameProjection>> getGamesWithRating() {
        var games = gameRepository.findGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    //Поменять на тип текст либо убрать аннотацию

    @Transactional
    @PostMapping("/{gameId}/set-designer/{designerId}")
    public ResponseEntity<GameDTO> addDesignerToGame(@PathVariable Integer gameId, @PathVariable Integer designerId){
        var game = gameRepository.findGameById(gameId);
        var designer = designerRepository.findDesignerById(designerId);

        var gameByDesigner = new GameByDesigner();
        gameByDesigner.setDesigner(designer);
        gameByDesigner.setGame(game);

        gameByDesignerRepository.save(gameByDesigner);

        var gameDTO = gameService.projectionsToGameDTO(gameRepository.findGame(gameId),
                designerRepository.findDesignersUsingGame(gameId));

        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{gameId}/remove-designer/{gameByDesignerId}")
    public ResponseEntity<GameDTO> deleteDesignerFromGame(@PathVariable Integer gameId, @PathVariable Integer gameByDesignerId){

        gameByDesignerRepository.deleteById(gameByDesignerId);

        var gameDTO = gameService.projectionsToGameDTO(gameRepository.findGame(gameId),
                designerRepository.findDesignersUsingGame(gameId));

        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }


    // todo: use standard hibernate deleteById and findById!!!
    //todo: ratings list
}
