package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Integer> {
}