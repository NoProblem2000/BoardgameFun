package com.petproject.boardgamefun.dto.projection;

import com.petproject.boardgamefun.model.User;

public interface UsersGameRatingProjection {
    User getUser();
    Double getRating();
}
