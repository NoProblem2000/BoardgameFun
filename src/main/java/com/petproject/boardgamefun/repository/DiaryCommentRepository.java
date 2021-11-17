package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.DiaryComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryCommentRepository extends JpaRepository<DiaryComment, Integer> {
}