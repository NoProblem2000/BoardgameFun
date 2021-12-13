package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.ForumMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumMessageRepository extends JpaRepository<ForumMessage, Integer> {
    List<ForumMessage> findByUserId(Integer id);
    List<ForumMessage> findByForumId(Integer id);
    ForumMessage findForumMessageById(Integer id);
}