package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.projection.UsersGameRatingProjection;
import com.petproject.boardgamefun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(Integer id);
    User findUserByName(String name);
    Boolean existsByName(String username);
    Boolean existsByMail(String email);

    @Query("select u as user, rgbu.rating as rating from User u " +
            "join RatingGameByUser rgbu on rgbu.user.id = u.id " +
            "join Game g on g.id = rgbu.game.id " +
            "where g.id = :gameId")
    List<UsersGameRatingProjection> findGameRatings(Integer gameId);
}