package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Game;

public interface GameRatingDTO {
    Game getGame();
    Integer getRating();
}