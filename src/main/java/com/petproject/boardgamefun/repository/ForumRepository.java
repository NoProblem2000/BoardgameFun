package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Integer> {
    List<Forum> findForumsByGameId(Integer id);
    List<Forum> findForumsByUserId(Integer id);
    Forum findForumById(Integer id);
}