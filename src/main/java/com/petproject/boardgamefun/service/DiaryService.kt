package com.petproject.boardgamefun.service

import com.petproject.boardgamefun.dto.projection.DiaryWithRatingsProjection
import com.petproject.boardgamefun.dto.DiaryDTO
import com.petproject.boardgamefun.model.Diary
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
open class DiaryService {
    open fun projectionsToDiaryDTO(projections: List<DiaryWithRatingsProjection>): List<DiaryDTO> {
        val diaries: MutableList<DiaryDTO> = ArrayList()
        for (projection in projections) {
            diaries.add(DiaryDTO(projection.diary, projection.rating))
        }
        return diaries
    }

    open fun entityToDiaryDTO(diary: Diary): DiaryDTO {
        return DiaryDTO(diary, 0.0)
    }

    open fun projectionToDiaryDTO(projection: DiaryWithRatingsProjection): DiaryDTO {
        return DiaryDTO(projection.diary, projection.rating)
    }
}