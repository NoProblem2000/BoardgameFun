package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DiaryDTO;
import com.petproject.boardgamefun.dto.projection.DiaryWithRatingsProjection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryService {
    public List<DiaryDTO> projectionsToDiaryDTO(List<DiaryWithRatingsProjection> projections) {
        List<DiaryDTO> diaries = new ArrayList<>();
        for (var projection : projections) {
            diaries.add(new DiaryDTO(projection.getDiary(), projection.getRating()));
        }

        return diaries;
    }

    public DiaryDTO projectionToDiaryDTO(DiaryWithRatingsProjection projection) {
        DiaryDTO diary = new DiaryDTO();
        diary.setDiary(projection.getDiary());
        diary.setRating(projection.getRating());

        return diary;
    }
}

