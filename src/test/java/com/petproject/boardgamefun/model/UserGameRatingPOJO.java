package com.petproject.boardgamefun.model;

import com.petproject.boardgamefun.dto.projection.UserGameRatingProjection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGameRatingPOJO implements UserGameRatingProjection {

    Game game;
    Integer rating;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Integer getRating() {
        return rating;
    }
}
