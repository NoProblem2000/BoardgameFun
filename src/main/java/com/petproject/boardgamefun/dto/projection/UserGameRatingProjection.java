package com.petproject.boardgamefun.dto.projection;

import com.petproject.boardgamefun.model.Game;

public interface UserGameRatingProjection {
    Game getGame();
    Integer getRating();
}