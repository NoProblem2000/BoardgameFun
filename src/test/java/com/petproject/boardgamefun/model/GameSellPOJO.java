package com.petproject.boardgamefun.model;

import com.petproject.boardgamefun.dto.projection.GameSellProjection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameSellPOJO implements GameSellProjection {

    private Game game;
    private String condition;
    private String comment;
    private Integer price;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public Integer getPrice() {
        return price;
    }
}
