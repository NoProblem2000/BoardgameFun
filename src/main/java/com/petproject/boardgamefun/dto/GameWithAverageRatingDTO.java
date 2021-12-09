package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Game;

public interface GameWithAverageRatingDTO {
    Game getGame();
    Double getRating();
}
