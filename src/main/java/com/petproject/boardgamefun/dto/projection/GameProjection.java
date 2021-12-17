package com.petproject.boardgamefun.dto.projection;

import com.petproject.boardgamefun.model.Game;


public interface GameProjection {
    Game getGame();
    Double getRating();
}
