package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.UsersGameRatingDTO;
import com.petproject.boardgamefun.dto.projection.*;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.model.Game;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {
    public GameDTO projectionsToGameDTO(GameProjection gameProjection, List<DesignersProjection> designersProjection) {
        return new GameDTO(gameProjection.getGame(),
                gameProjection.getRating(),
                designersProjection.stream().map(DesignersProjection::getDesigner).collect(Collectors.toList()));
    }

    public GameDTO projectionToGameDTO(GameProjection gameProjection) {
        return new GameDTO(gameProjection.getGame(), null, null);
    }

    public List<GameDTO> projectionsToGameDTO(List<GameProjection> gameProjections) {
        List<GameDTO> games = new ArrayList<>();
        for (var game :
                gameProjections) {
            games.add(new GameDTO(game.getGame(), game.getRating(), null));
        }
        return games;
    }


    public List<GameDTO> entitiesToGameDTO(List<Game> games) {
        ArrayList<GameDTO> gamesDTO = new ArrayList<>();
        for (var game :
                games) {
            gamesDTO.add(new GameDTO(game, null, null));
        }
        return gamesDTO;
    }

    public GameDTO entityToGameDTO(Game game){
        return new GameDTO(game, null, null);
    }

    public List<String> getTitlesFromProjections(List<GamesFilterByTitleProjection> games) {
        return games.stream().map(GamesFilterByTitleProjection::getTitle).collect(Collectors.toList());
    }

    public List<GameDTO> userGameRatingToGameDTO(List<UserGameRatingProjection> projections) {
        ArrayList<GameDTO> games = new ArrayList<>();
        for (var projection :
                projections) {
            games.add(new GameDTO(projection.getGame(), (double) projection.getRating(), null));
        }
        return games;
    }

    public List<UsersGameRatingDTO> usersGameRatingToDTO(List<UsersGameRatingProjection> projections) {
        ArrayList<UsersGameRatingDTO> users = new ArrayList<>();
        for (var projection :
                projections) {
            users.add(new UsersGameRatingDTO(projection.getUser(), projection.getRating()));
        }
        return users;
    }
}
