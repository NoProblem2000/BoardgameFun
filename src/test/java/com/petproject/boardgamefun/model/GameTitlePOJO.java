package com.petproject.boardgamefun.model;

import com.petproject.boardgamefun.dto.projection.GamesFilterByTitleProjection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameTitlePOJO implements GamesFilterByTitleProjection {

    private String title;
    private Integer id;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
