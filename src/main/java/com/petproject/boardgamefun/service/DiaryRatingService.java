package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DiaryRatingDTO;
import com.petproject.boardgamefun.dto.request.DiaryRatingRequest;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.DiaryRating;
import com.petproject.boardgamefun.repository.DiaryRatingRepository;
import com.petproject.boardgamefun.repository.DiaryRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DiaryRatingService {

    final DiaryRatingRepository diaryRatingRepository;
    final DiaryRepository diaryRepository;
    final UserRepository userRepository;

    public DiaryRatingService(DiaryRatingRepository diaryRatingRepository, DiaryRepository diaryRepository, UserRepository userRepository) {
        this.diaryRatingRepository = diaryRatingRepository;
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DiaryRatingDTO setDiaryRating(Integer diaryId, Integer userId, DiaryRatingRequest ratingRequest) {
        var diary = diaryRepository.findDiaryById(diaryId);
        var user = userRepository.findUserById(userId);

        if (user == null || diary == null) {
            throw new NoRecordFoundException("User or diary not found");
        }

        var diaryRating = new DiaryRating();
        diaryRating.setDiary(diary);
        diaryRating.setUser(user);
        diaryRating.setRating(ratingRequest.getRating());
        diaryRatingRepository.save(diaryRating);
        return entityToDiaryRatingDTO(diaryRating);
    }

    @Transactional
    public DiaryRatingDTO updateDiaryRating(Integer ratingId, DiaryRatingRequest ratingRequest) {
        var diaryRating = diaryRatingRepository.findDiaryRatingById(ratingId);

        if (diaryRating == null) {
            throw new NoRecordFoundException("DiaryRating not found");
        }

        if (ratingRequest != null && !Objects.equals(diaryRating.getRating(), ratingRequest.getRating())) {
            diaryRating.setRating(ratingRequest.getRating());
        }

        diaryRatingRepository.save(diaryRating);

        return entityToDiaryRatingDTO(diaryRating);
    }

    @Transactional
    public void deleteDiaryRating(Integer ratingId) {
        var diaryRating = diaryRatingRepository.findDiaryRatingById(ratingId);
        if (diaryRating == null) {
            throw new NoRecordFoundException("Diary rating not found");
        }
        diaryRatingRepository.delete(diaryRating);
    }

    public List<DiaryRatingDTO> entitiesToDiaryRatingDTO(List<DiaryRating> diaryRatings) {
        ArrayList<DiaryRatingDTO> diaryRatingsDTO = new ArrayList<>();
        for (var diaryRating :
                diaryRatings) {
            diaryRatingsDTO.add(new DiaryRatingDTO(diaryRating.getId(), diaryRating.getRating()));
        }
        return diaryRatingsDTO;
    }

    public DiaryRatingDTO entityToDiaryRatingDTO(DiaryRating diaryRating) {
        return new DiaryRatingDTO(diaryRating.getId(), diaryRating.getRating());
    }
}
