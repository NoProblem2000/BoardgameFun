package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Game;

public interface UserGameRatingDTO {
    Game getGame();
    Integer getRating();
}