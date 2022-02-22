package com.petproject.boardgamefun.dto.projection

import com.petproject.boardgamefun.model.Diary

interface DiaryWithRatingsProjection {
    val diary: Diary
    val rating: Double?
}