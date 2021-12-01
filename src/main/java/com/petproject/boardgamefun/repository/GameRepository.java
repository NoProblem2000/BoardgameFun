package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.GameRatingDTO;
import com.petproject.boardgamefun.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findGameByTitle(String title);
    Game findGameById(Integer id);

    @Query("Select g from Game g " +
            "join UserOwnGame uog on g.id = uog.game.id " +
            "join User u on u.id = uog.user.id " +
            "where u.id = :id")
    List<Game> findUserGames(Integer id);

    @Query("Select g as game, rgbu.rating as rating from Game g " +
            "join RatingGameByUser rgbu on rgbu.game.id = g.id " +
            "join User u on u.id = rgbu.user.id " +
            "where u.id = :id " +
            "order by rgbu.rating desc")
    List<GameRatingDTO> findGameRatingList(Integer id);
}