package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.FilterGamesDTO;
import com.petproject.boardgamefun.dto.RatingGameByUserDTO;
import com.petproject.boardgamefun.dto.projection.*;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.exception.BadRequestException;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.repository.*;
import com.petproject.boardgamefun.service.mappers.GameMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    final GameMapper gameMapper;
    final GameRepository gameRepository;
    final DesignerRepository designerRepository;

    public GameService(GameMapper gameMapper, GameRepository gameRepository, DesignerRepository designerRepository) {
        this.gameMapper = gameMapper;
        this.gameRepository = gameRepository;
        this.designerRepository = designerRepository;
    }

    @Transactional
    public List<GameDataDTO> getGames()
    {
        return projectionsToGameDTO(gameRepository.findGames());
    }

    @Transactional
    public GameDataDTO getGameById(Integer id){
        var gameDataDTO = projectionsToGameDTO(gameRepository.findGameWithRating(id), designerRepository.findDesignersUsingGame(id));
        if (gameDataDTO == null) {
            throw new NoRecordFoundException("No game with id " + id + " found");
        }
        return gameDataDTO;
    }

    @Transactional
    public List<FilterGamesDTO> getGameByTitle (String title) {
        return getTitlesFromProjections(gameRepository.findGamesUsingTitle(title));
    }

    @Transactional
    public GameDataDTO uploadImage (Integer gameId, MultipartFile multipartFile) throws IOException {
        var game = gameRepository.findGameById(gameId);
        if (game == null)
            throw new NoRecordFoundException("Game not found");
        game.setPicture(multipartFile.getBytes());
        gameRepository.save(game);
        return projectionsToGameDTO(gameRepository.findGameWithRating(gameId), designerRepository.findDesignersUsingGame(gameId));
    }

    @Transactional
    public void checkExistence(Game newGame) {
        if (newGame.getId() != null) {
            throw new BadRequestException("Game already exists");
        }
    }

    @Transactional
    public GameDataDTO save(Game updatedGame) {
        if (updatedGame.getTitle() == null || updatedGame.getAnnotation() == null || updatedGame.getDescription() == null || updatedGame.getTimeToPlayMax() == null || updatedGame.getTimeToPlayMin() == null || updatedGame.getYearOfRelease() == null || updatedGame.getPlayersMin() == null || updatedGame.getPlayersMax() == null) {
            throw new BadRequestException("Invalid game fields");
        }
        gameRepository.save(updatedGame);
        return entityToGameDTO(updatedGame);
    }

    @Transactional
    public List<GameDataDTO> getUserCollection(Integer userId){
        return entitiesToGameDTO(gameRepository.findUserGames(userId));
    }

    @Transactional
    public void delete(Game game) {
        if (game.getId() == null)
            throw new NoRecordFoundException("Game does not exist");
        gameRepository.delete(game);
    }

    public GameDataDTO projectionsToGameDTO(GameProjection gameProjection, List<DesignersProjection> designersProjection) {
        if (gameProjection == null)
            return null;
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
