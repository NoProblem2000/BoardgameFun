package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DesignersProjection;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.GameProjection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    public GameDTO projectionsToGameDTO(GameProjection gameProjection, List<DesignersProjection> designersProjection){
        return new GameDTO(gameProjection.getGame(),
                gameProjection.getRating(),
                designersProjection.stream().map(DesignersProjection::getDesigner).collect(Collectors.toList()));
    }
}
