package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
}