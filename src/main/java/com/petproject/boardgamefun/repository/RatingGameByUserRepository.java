package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.RatingGameByUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingGameByUserRepository extends JpaRepository<RatingGameByUser, Integer> {
    RatingGameByUser findRatingGame_ByUserIdAndGameId(Integer userId, Integer gameId );
}