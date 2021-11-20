package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.ForumMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumMessageRepository extends JpaRepository<ForumMessage, Integer> {
}