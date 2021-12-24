package com.petproject.boardgamefun.dto.projection;

import com.petproject.boardgamefun.model.Diary;

public interface DiaryWithRatingsProjection {
    Diary getDiary();
    Double getRating();
}
