package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.FilterGamesDTO;
import com.petproject.boardgamefun.dto.RatingGameByUserDTO;
import com.petproject.boardgamefun.dto.projection.*;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.service.mappers.GameMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    final
    GameMapper gameMapper;

    public GameService(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
    }

    public GameDataDTO projectionsToGameDTO(GameProjection gameProjection, List<DesignersProjection> designersProjection) {
        return new GameDataDTO(gameMapper.gameToGameDTO(gameProjection.getGame()),
                gameProjection.getRating(),
                designersProjection.stream().map(DesignersProjection::getDesigner).collect(Collectors.toList()));
    }

    public GameDataDTO projectionToGameDTO(GameProjection gameProjection) {
        return new GameDataDTO(gameMapper.gameToGameDTO(gameProjection.getGame()), null, null);
    }

    public List<GameDataDTO> projectionsToGameDTO(List<GameProjection> gameProjections) {
        List<GameDataDTO> games = new ArrayList<>();
        for (var game :
                gameProjections) {
            games.add(new GameDataDTO(gameMapper.gameToGameDTO(game.getGame()), game.getRating(), null));
        }
        return games;
    }


    public List<GameDataDTO> entitiesToGameDTO(List<Game> games) {
        ArrayList<GameDataDTO> gamesDTO = new ArrayList<>();
        for (var game :
                games) {
            gamesDTO.add(new GameDataDTO(gameMapper.gameToGameDTO(game), null, null));
        }
        return gamesDTO;
    }

    public GameDataDTO entityToGameDTO(Game game) {
        return new GameDataDTO(gameMapper.gameToGameDTO(game), null, null);
    }

    public List<FilterGamesDTO> getTitlesFromProjections(List<GamesFilterByTitleProjection> projections) {
        ArrayList<FilterGamesDTO> games = new ArrayList<>();
        for (var projection : projections) {
            games.add(new FilterGamesDTO(projection.getId(), projection.getTitle()));
        }
        return games;
    }

    public List<GameDataDTO> userGameRatingToGameDTO(List<UserGameRatingProjection> projections) {
        ArrayList<GameDataDTO> games = new ArrayList<>();
        for (var projection :
                projections) {
            games.add(new GameDataDTO(gameMapper.gameToGameDTO(projection.getGame()), (double) projection.getRating(), null));
        }
        return games;
    }

    public List<RatingGameByUserDTO> usersGameRatingToDTO(List<UsersGameRatingProjection> projections) {
        ArrayList<RatingGameByUserDTO> users = new ArrayList<>();
        for (var projection :
                projections) {
            users.add(new RatingGameByUserDTO(projection.getUser().getId(), projection.getRating()));
        }
        return users;
    }
}
