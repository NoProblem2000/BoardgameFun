package com.petproject.boardgamefun.model;

import com.petproject.boardgamefun.dto.projection.GameProjection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GamePOJO implements GameProjection {
    private Game game;

    private Double rating;


    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Double getRating() {
        return rating;
    }
}
