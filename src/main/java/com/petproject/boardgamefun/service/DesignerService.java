package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.Designer;
import com.petproject.boardgamefun.model.GameByDesigner;
import com.petproject.boardgamefun.repository.DesignerRepository;
import com.petproject.boardgamefun.repository.GameByDesignerRepository;
import com.petproject.boardgamefun.repository.GameRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DesignerService {

    final GameRepository gameRepository;
    final GameByDesignerRepository gameByDesignerRepository;
    final GameService gameService;
    final DesignerRepository designerRepository;
    public DesignerService(GameRepository gameRepository, GameByDesignerRepository gameByDesignerRepository, GameService gameService, DesignerRepository designerRepository) {
        this.gameRepository = gameRepository;
        this.gameByDesignerRepository = gameByDesignerRepository;
        this.gameService = gameService;
        this.designerRepository = designerRepository;
    }

    @Transactional
    public GameDataDTO addDesignerToGame(Integer gameId, Integer designerId) {
        var game = gameRepository.findGameById(gameId);
        var designer = designerRepository.findDesignerById(designerId);

        if (game == null || designer == null){
            throw new NoRecordFoundException("Game or designer not found");
        }

        var gameByDesigner = new GameByDesigner();
        gameByDesigner.setDesigner(designer);
        gameByDesigner.setGame(game);

        gameByDesignerRepository.save(gameByDesigner);
        return gameService.projectionsToGameDTO(gameRepository.findGameWithRating(gameId),
                designerRepository.findDesignersUsingGame(gameId));
    }

    @Transactional
    public GameDataDTO deleteDesignerFromGame(Integer gameId, Integer gameByDesignerId) {
        gameByDesignerRepository.deleteById(gameByDesignerId);
        return gameService.projectionsToGameDTO(gameRepository.findGameWithRating(gameId),
                designerRepository.findDesignersUsingGame(gameId));
    }

    public List<DesignerDTO> entitiesToDesignerDTO(List<Designer> designers) {
        List<DesignerDTO> designersDTO = new ArrayList<>();

        for (var item :
                designers) {
            designersDTO.add(new DesignerDTO(item.getId(), item.getName()));
        }
        return designersDTO;
    }

    public DesignerDTO entityToDesignerDTO(Designer designer){
        return new DesignerDTO(designer.getId(), designer.getName());
    }
}
