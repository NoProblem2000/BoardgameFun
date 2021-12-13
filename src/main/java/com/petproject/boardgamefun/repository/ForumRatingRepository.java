package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.ForumRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRatingRepository extends JpaRepository<ForumRating, Integer> {
    ForumRating findForumRating_ByForumIdAndUserId(Integer forumId, Integer userId);
    ForumRating findForumRatingById(Integer id);
}