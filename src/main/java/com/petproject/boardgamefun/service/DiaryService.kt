package com.petproject.boardgamefun.service

import com.petproject.boardgamefun.dto.projection.DiaryWithRatingsProjection
import com.petproject.boardgamefun.dto.DiaryDataDTO
import com.petproject.boardgamefun.model.Diary
import com.petproject.boardgamefun.service.mappers.DiaryMapper
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class DiaryService (private val diaryMapper: DiaryMapper) {

    fun projectionsToDiaryDTO(projections: List<DiaryWithRatingsProjection>): List<DiaryDataDTO> {
        val diaries: MutableList<DiaryDataDTO> = ArrayList()
        for (projection in projections) {
            diaries.add(DiaryDataDTO(diaryMapper.diaryToDiaryDTO(projection.diary), projection.rating))
        }
        return diaries
    }

    fun entityToDiaryDTO(diary: Diary): DiaryDataDTO {
        return DiaryDataDTO(diaryMapper.diaryToDiaryDTO(diary), 0.0)
    }

    fun projectionToDiaryDTO(projection: DiaryWithRatingsProjection): DiaryDataDTO {
        return DiaryDataDTO(diaryMapper.diaryToDiaryDTO(projection.diary), projection.rating)
    }
}