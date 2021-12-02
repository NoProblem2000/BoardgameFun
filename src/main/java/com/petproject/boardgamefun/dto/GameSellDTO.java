package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Game;

public interface GameSellDTO {
    Game getGame();
    String getCondition();
    String getComment();
    Integer getPrice();
}
