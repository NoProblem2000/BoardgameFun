package com.petproject.boardgamefun.model;

import com.petproject.boardgamefun.dto.projection.UsersGameRatingProjection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsersGameRatingProjectionPOJO implements UsersGameRatingProjection {
    private User user;
    private Double rating;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Double getRating() {
        return rating;
    }
}
