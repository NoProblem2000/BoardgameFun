package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.UsersGameRatingDTO;
import com.petproject.boardgamefun.dto.projection.*;
import com.petproject.boardgamefun.dto.GameDTO;
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
