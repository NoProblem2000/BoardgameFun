package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DiaryRatingDTO;
import com.petproject.boardgamefun.model.DiaryRating;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryRatingService {
    public List<DiaryRatingDTO> entitiesToDiaryRatingDTO(List<DiaryRating> diaryRatings){
        ArrayList<DiaryRatingDTO> diaryRatingsDTO = new ArrayList<>();
        for (var diaryRating :
                diaryRatings) {
            diaryRatingsDTO.add(new DiaryRatingDTO(diaryRating.getId(), diaryRating.getRating()));
        }
        return diaryRatingsDTO;
    }

    public DiaryRatingDTO entityToDiaryRatingDTO(DiaryRating diaryRating){
        return new DiaryRatingDTO(diaryRating.getId(), diaryRating.getRating());
    }
}
