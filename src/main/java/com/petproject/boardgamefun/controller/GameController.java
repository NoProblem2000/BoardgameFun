package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.FilterGamesDTO;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.UsersGameRatingDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    ResponseEntity<List<GameDTO>> getGames() {

        var games = gameService.projectionsToGameDTO(gameRepository.findGames());

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
    public ResponseEntity<List<FilterGamesDTO>> getGamesByTitle(@PathVariable String title) {
        var games = gameService.getTitlesFromProjections(gameRepository.findGamesUsingTitle(title));
        return new ResponseEntity<>(games, HttpStatus.OK);
    }


    @Transactional
    @PostMapping("/add")
    public ResponseEntity<GameDTO> addGame(@RequestBody Game newGame) {
        gameRepository.save(newGame);
        var game = gameService.entityToGameDTO(newGame);

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/upload-image/{gameId}")
    public ResponseEntity<GameDTO> uploadImage(@PathVariable Integer gameId, @RequestParam("picture") MultipartFile file) throws IOException {
        var game = gameRepository.findGameById(gameId);
        game.setPicture(file.getBytes());
        gameRepository.save(game);
        var gameDTO = gameService.projectionsToGameDTO(gameRepository.findGame(gameId), designerRepository.findDesignersUsingGame(gameId));
        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<GameDTO> updateGame(@RequestBody Game updatedGame) {
        gameRepository.save(updatedGame);
        var game = gameService.entityToGameDTO(updatedGame);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeGameFromSite(@RequestBody Game deleteGame) {
        gameRepository.delete(deleteGame);

        return new ResponseEntity<>(deleteGame.getTitle() + " удалена из базы данных", HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/expansions/{gameId}")
    public ResponseEntity<List<GameDTO>> getExpansions(@PathVariable Integer gameId) {
        var gamesExpansions = gameService.entitiesToGameDTO(gameRepository.getExpansions(gameId));

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add-expansion/{parentGameId}/{daughterGameId}")
    public ResponseEntity<List<GameDTO>> addExpansion(@PathVariable Integer parentGameId, @PathVariable Integer daughterGameId) {
        var parentGame = gameRepository.findGameById(parentGameId);
        var daughterGame = gameRepository.findGameById(daughterGameId);

        var expansion = new Expansion();
        expansion.setParentGame(parentGame);
        expansion.setDaughterGame(daughterGame);
        expansionRepository.save(expansion);

        var gamesExpansions = gameService.entitiesToGameDTO(gameRepository.getExpansions(parentGameId));

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-expansion/{parentGameId}/{daughterGameId}")
    public ResponseEntity<List<GameDTO>> deleteExpansion(@PathVariable Integer daughterGameId, @PathVariable Integer parentGameId) {
        var expansion = expansionRepository.findExpansion_ByDaughterGameIdAndParentGameId(daughterGameId, parentGameId);
        expansionRepository.delete(expansion);

        var gamesExpansions = gameService.entitiesToGameDTO(gameRepository.getExpansions(parentGameId));

        return new ResponseEntity<>(gamesExpansions, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/similar/{gameId}")
    public ResponseEntity<List<GameDTO>> getSimilarGames(@PathVariable Integer gameId) {
        var similarGames = gameService.entitiesToGameDTO(gameRepository.getSimilarGames(gameId));

        return new ResponseEntity<>(similarGames, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add-similar/{referenceGameId}/{sourceGameId}")
    public ResponseEntity<List<GameDTO>> addSimilarGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {
        var referenceGame = gameRepository.findGameById(referenceGameId);
        var sourceGame = gameRepository.findGameById(sourceGameId);

        var sameGame = new SameGame();
        sameGame.setReferenceGame(referenceGame);
        sameGame.setSourceGame(sourceGame);
        sameGameRepository.save(sameGame);

        var sameGames = gameService.entitiesToGameDTO(gameRepository.getSimilarGames(referenceGameId));

        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-similar/{referenceGameId}/{sourceGameId}")
    public ResponseEntity<List<GameDTO>> deleteSameGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {
        var sameGame = sameGameRepository.findSameGame_ByReferenceGameIdAndSourceGameId(referenceGameId, sourceGameId);
        sameGameRepository.delete(sameGame);

        var sameGames = gameService.entitiesToGameDTO(gameRepository.getSimilarGames(referenceGameId));

        return new ResponseEntity<>(sameGames, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{gameId}/users-rating")
    public ResponseEntity<List<UsersGameRatingDTO>> getUsersRating(@PathVariable Integer gameId) {

        var ratings = gameService.usersGameRatingToDTO(userRepository.findGameRatings(gameId));

        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    //Поменять на тип текст либо убрать аннотацию

    @Transactional
    @PostMapping("/{gameId}/set-designer/{designerId}")
    public ResponseEntity<GameDTO> addDesignerToGame(@PathVariable Integer gameId, @PathVariable Integer designerId) {
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
    public ResponseEntity<GameDTO> deleteDesignerFromGame(@PathVariable Integer gameId, @PathVariable Integer gameByDesignerId) {

        gameByDesignerRepository.deleteById(gameByDesignerId);

        var gameDTO = gameService.projectionsToGameDTO(gameRepository.findGame(gameId),
                designerRepository.findDesignersUsingGame(gameId));

        return new ResponseEntity<>(gameDTO, HttpStatus.OK);
    }


    // todo: use standard hibernate deleteById and findById!!!
    //todo: ratings list
}
