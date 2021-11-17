package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.UserWish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWishRepository extends JpaRepository<UserWish, Integer> {
}