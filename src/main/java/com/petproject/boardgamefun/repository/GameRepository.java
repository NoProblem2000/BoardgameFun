package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.UserGameRatingDTO;
import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findGameById(Integer id);

    @Query("select g as game, avg(rgbu.rating) as rating from Game g " +
            "left join RatingGameByUser rgbu on rgbu.game.id = g.id " +
            "where g.id = :id " +
            "group by g")
    GameDTO findGame(Integer id);

    @Query("select g as game, avg(rgbu.rating) as rating from Game g " +
            "left join RatingGameByUser rgbu on rgbu.game.id = g.id " +
            "where g.title = :title " +
            "group by g")
    GameDTO findGameUsingTitle(String title);

    @Query("select g as game, avg(rgbu.rating) as rating from Game g " +
            "left join RatingGameByUser rgbu on rgbu.game.id = g.id " +
            "group by g")
    List<GameDTO> findGames();

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
    List<UserGameRatingDTO> findUserGameRatingList(Integer id);

    @Query("Select g from Game g " +
            "join UserWish uw on g.id = uw.game.id " +
            "join User u on u.id = uw.user.id " +
            "where u.id = :id")
    List<Game> findUserWishlist(Integer id);

    @Query("Select g as game, gs.comment as comment, gs.condition as condition, gs.price as price " +
            "from Game g " +
            "join GameSell gs on g.id = gs.game.id " +
            "join User u on u.id = gs.user.id " +
            "where u.id = :id")
    List<GameSellDTO> getGameSellList(Integer id);

    @Query("select g from Game g " +
            "join Expansion ex on ex.daughterGame.id = g.id " +
            "where ex.parentGame.id = :parentGameId")
    List<Game> getExpansions(Integer parentGameId);

    @Query("select g from Game g " +
            "join SameGame sg on sg.sourceGame.id = g.id " +
            "where sg.referenceGame.id = :referenceGameId")
    List<Game> getSimilarGames(Integer referenceGameId);
}