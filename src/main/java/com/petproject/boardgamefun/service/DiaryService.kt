package com.petproject.boardgamefun.service

import com.petproject.boardgamefun.dto.projection.DiaryWithRatingsProjection
import com.petproject.boardgamefun.dto.DiaryDTO
import com.petproject.boardgamefun.model.Diary
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class DiaryService {
    fun projectionsToDiaryDTO(projections: List<DiaryWithRatingsProjection>): List<DiaryDTO> {
        val diaries: MutableList<DiaryDTO> = ArrayList()
        for (projection in projections) {
            diaries.add(DiaryDTO(projection.diary, projection.rating))
        }
        return diaries
    }

    fun entityToDiaryDTO(diary: Diary): DiaryDTO {
        return DiaryDTO(diary, 0.0)
    }

    fun projectionToDiaryDTO(projection: DiaryWithRatingsProjection): DiaryDTO {
        return DiaryDTO(projection.diary, projection.rating)
    }
}