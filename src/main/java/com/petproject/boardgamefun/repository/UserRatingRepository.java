package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingRepository extends JpaRepository<UserRating, Integer> {
}