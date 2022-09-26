package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.request.DesignerRequest;
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

    @Transactional
    public List<DesignerDTO> getDesigners(){
        return entitiesToDesignerDTO(designerRepository.findAll());
    }

    @Transactional
    public DesignerDTO getDesignerByName(String name) {
        var designer = designerRepository.findDesignerByName(name);
        if (designer == null)
            throw new NoRecordFoundException("Designer " + name + " not found");
        return entityToDesignerDTO(designer);
    }

    @Transactional
    public List<DesignerDTO> addDesigner(DesignerRequest designerRequest){
        var designer = new Designer();
        designer.setName(designerRequest.getName());
        designerRepository.save(designer);

        return entitiesToDesignerDTO(designerRepository.findAll());
    }

    @Transactional
    public List<DesignerDTO> updateDesigner(Integer id, DesignerRequest designerRequest){
        var designer = designerRepository.findDesignerById(id);

        designer.setName(designerRequest.getName());
        designerRepository.save(designer);

        return entitiesToDesignerDTO(designerRepository.findAll());
    }

    @Transactional
    public List<DesignerDTO> deleteDesigner(Integer id){
        var designer = designerRepository.findDesignerById(id);
        designerRepository.delete(designer);
        return entitiesToDesignerDTO(designerRepository.findAll());
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
