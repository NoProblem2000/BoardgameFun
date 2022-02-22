package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.projection.ForumProjection;
import com.petproject.boardgamefun.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ForumRepository extends JpaRepository<Forum, Integer> {
    @Query("select f as forum, avg(fr.rating) as rating from Forum f " +
            "join Game g on g.id = f.game.id " +
            "left join ForumRating fr on fr.forum.id = f.id " +
            "where g.id = :gameId " +
            "group by f")
    List<ForumProjection> findForumsGameWithRating(Integer gameId);

    @Query("select f as forum, avg(fr.rating) as rating from Forum f " +
            "join User u on u.id = f.user.id " +
            "left join ForumRating fr on fr.forum.id = f.id " +
            "where u.id = :userId " +
            "group by f")
    List<ForumProjection> findForumsUserWithRating(Integer userId);

    @Query("select f as forum, avg(fr.rating) as rating from Forum f " +
           "left join ForumRating fr on fr.forum.id = f.id " +
            "group by f")
    List<ForumProjection> findForumsWithRating();

    @Query("select f as forum, avg(fr.rating) as rating from Forum f " +
            "left join ForumRating fr on fr.forum.id = f.id " +
            "where f.id = :id " +
            "group by f")
    ForumProjection findForumWithRatingUsingId(Integer id);

   Forum findForumById(Integer id);
}