package com.petproject.boardgamefun.dto.projection;

import com.petproject.boardgamefun.model.Diary;

public interface DiariesWithRatingsProjection {
    Diary getDiary();
    Double getRating();
}
