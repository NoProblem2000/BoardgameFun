package com.petproject.boardgamefun.dto.projection;

import com.petproject.boardgamefun.model.Game;

public interface GameSellProjection {
    Game getGame();
    String getCondition();
    String getComment();
    Integer getPrice();
}
