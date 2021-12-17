package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Game;


public interface GameProjection {
    Game getGame();
    Double getRating();
}
