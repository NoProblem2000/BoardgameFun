package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.dto.projection.GameSellProjection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameSellService {
    public List<GameSellDTO> projectionsToGameSellDTO(List<GameSellProjection> projections){
        ArrayList<GameSellDTO> gamesForSell = new ArrayList<>();
        for (var projection :
                projections) {
            gamesForSell.add(new GameSellDTO(projection.getGame(),
                    projection.getCondition(),
                    projection.getComment(),
                    projection.getPrice()
            ));
        }

        return gamesForSell;

    }
}
