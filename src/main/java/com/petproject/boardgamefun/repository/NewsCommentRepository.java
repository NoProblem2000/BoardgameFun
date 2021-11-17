package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.NewsComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCommentRepository extends JpaRepository<NewsComment, Integer> {
}