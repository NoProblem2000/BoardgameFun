package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.ForumDTO;
import com.petproject.boardgamefun.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Integer> {
    @Query("select f as forum, avg(fr.rating) as rating from Forum f " +
            "join Game g on g.id = f.game.id " +
            "join ForumRating fr on fr.forum.id = f.id " +
            "where g.id = :gameId " +
            "group by f")
    List<ForumDTO> findForumsGameWithRating(Integer gameId);

    @Query("select f as forum, avg(fr.rating) as rating from Forum f " +
            "join User u on u.id = f.user.id " +
            "join ForumRating fr on fr.forum.id = f.id " +
            "where u.id = :userId " +
            "group by f")
    List<ForumDTO> findForumsUserWithRating(Integer userId);

    @Query("select f as forum, avg(fr.rating) as rating from Forum f " +
           "join ForumRating fr on fr.forum.id = f.id " +
            "group by f")
    List<ForumDTO> findForumsWithRating();

   Forum findForumById(Integer id);
}