package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.Expansion;
import com.petproject.boardgamefun.repository.ExpansionRepository;
import com.petproject.boardgamefun.repository.GameRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ExpansionService {

    final ExpansionRepository expansionRepository;
    final GameRepository gameRepository;
    final GameService gameService;

    public ExpansionService(ExpansionRepository expansionRepository, GameRepository gameRepository, GameService gameService) {
        this.expansionRepository = expansionRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
    }

    @Transactional
    public List<GameDataDTO> getExpansions(Integer gameId) {
        if (gameId == null)
            throw  new NoRecordFoundException("Game does not exist with id " + gameId);

        return gameService.entitiesToGameDTO(gameRepository.getExpansions(gameId));
    }

    @Transactional
    public List<GameDataDTO> addExpansion(Integer parentGameId, Integer daughterGameId){
        var parentGame = gameRepository.findGameById(parentGameId);
        var daughterGame = gameRepository.findGameById(daughterGameId);

        if (parentGame == null || daughterGame == null) {
           throw new NoRecordFoundException("DaughterGame or ParentGame not found");
        }

        var expansion = new Expansion();
        expansion.setParentGame(parentGame);
        expansion.setDaughterGame(daughterGame);
        expansionRepository.save(expansion);

        return gameService.entitiesToGameDTO(gameRepository.getExpansions(parentGameId));
    }

    @Transactional
    public List<GameDataDTO> deleteExpansion(Integer parentGameId, Integer daughterGameId) {
        var expansion = expansionRepository.findExpansion_ByParentGameIdAndDaughterGameId(parentGameId, daughterGameId);
        if (expansion == null) {
            throw new NoRecordFoundException("Expansion not found for parent");
        }
        expansionRepository.delete(expansion);
        return gameService.entitiesToGameDTO(gameRepository.getExpansions(parentGameId));
    }

}
