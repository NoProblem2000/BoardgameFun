package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.dto.projection.GameSellProjection;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.service.mappers.GameMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameSellService {

    final GameMapper gameMapper;

    public GameSellService(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
    }

    public GameSellDTO entityToGameSellDTO(Game game) {
        return new GameSellDTO(gameMapper.gameToGameDTO(game), null, null, null);
    }

    public List<GameSellDTO> projectionsToGameSellDTO(List<GameSellProjection> projections) {
        ArrayList<GameSellDTO> gamesForSell = new ArrayList<>();
        for (var projection :
                projections) {
            gamesForSell.add(new GameSellDTO(gameMapper.gameToGameDTO(projection.getGame()),
                    projection.getCondition(),
                    projection.getComment(),
                    projection.getPrice()
            ));
        }

        return gamesForSell;

    }
}
