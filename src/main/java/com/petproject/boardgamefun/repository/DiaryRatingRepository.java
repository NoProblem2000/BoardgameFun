package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.DiaryRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRatingRepository extends JpaRepository<DiaryRating, Integer> {
}