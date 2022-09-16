package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.SameGame;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.SameGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SimilarGameService {

    final GameService gameService;
    final GameRepository gameRepository;
    final SameGameRepository sameGameRepository;

    public SimilarGameService(GameService gameService, GameRepository gameRepository, SameGameRepository sameGameRepository) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.sameGameRepository = sameGameRepository;
    }

    @Transactional
    public List<GameDataDTO> getSimilarGames(Integer gameId) {
        if (gameId == null)
            throw new NoRecordFoundException("Game with id " + gameId + " not found");
        return gameService.entitiesToGameDTO(gameRepository.getSimilarGames(gameId));
    }

    @Transactional
    public List<GameDataDTO> addSimilarGame(Integer referenceGameId, Integer sourceGameId) {
        var referenceGame = gameRepository.findGameById(referenceGameId);
        var sourceGame = gameRepository.findGameById(sourceGameId);

        if (referenceGame == null || sourceGame == null)
           throw new NoRecordFoundException("referenceGame or sourceGame are null");

        var sameGame = new SameGame();
        sameGame.setReferenceGame(referenceGame);
        sameGame.setSourceGame(sourceGame);
        sameGameRepository.save(sameGame);
        return gameService.entitiesToGameDTO(gameRepository.getSimilarGames(referenceGameId));
    }

    @Transactional
    public List<GameDataDTO> deleteSameGame(@PathVariable Integer referenceGameId, @PathVariable Integer sourceGameId) {
        var sameGame = sameGameRepository.findSameGame_ByReferenceGameIdAndSourceGameId(referenceGameId, sourceGameId);
        if (sameGame == null) {
            throw new NoRecordFoundException("No same game found for reference");
        }
        sameGameRepository.delete(sameGame);

        return gameService.entitiesToGameDTO(gameRepository.getSimilarGames(referenceGameId));
    }
}
