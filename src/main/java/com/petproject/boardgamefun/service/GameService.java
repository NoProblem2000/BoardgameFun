package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.projection.DesignersProjection;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.projection.GamesFilterByTitleProjection;
import com.petproject.boardgamefun.dto.projection.GameProjection;
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

    public List<String> getTitlesFromProjections(List<GamesFilterByTitleProjection> games){
        return games.stream().map(GamesFilterByTitleProjection::getTitle).collect(Collectors.toList());
    }
}
