package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.dto.projection.GameSellProjection;
import com.petproject.boardgamefun.exception.BadRequestException;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.model.GameSell;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.GameSellRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.service.mappers.GameMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameSellService {

    final GameMapper gameMapper;
    final GameSellRepository gameSellRepository;
    final UserRepository userRepository;
    final GameRepository gameRepository;

    public GameSellService(GameMapper gameMapper, GameSellRepository gameSellRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.gameMapper = gameMapper;
        this.gameSellRepository = gameSellRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public List<GameSellDTO> addGameToSellList(Integer userId, Integer gameId, GameSell gameSell) {
        if (gameSell.getId() != null) {
            throw new BadRequestException("Game for sell is already exist");
        }

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        if (game == null || user == null) {
            throw new NoRecordFoundException("User or game not found");
        }

        gameSell.setGame(game);
        gameSell.setUser(user);

        gameSellRepository.save(gameSell);
        return projectionsToGameSellDTO(gameRepository.getGameSellList(userId));
    }

    @Transactional
    public void removeGameFromSell(Integer userId, Integer gameId) {
        var gameSell = gameSellRepository.findGameSell_ByUserIdAndGameId(userId, gameId);
        if (gameSell == null) {
            throw new NoRecordFoundException("No game for sell is found");
        }
        gameSellRepository.delete(gameSell);
    }

   @Transactional
   public void updateGameSell(GameSell gameSell) {
        if (gameSell.getId() == null || gameSell.getGame() == null || gameSell.getUser() == null) {
            throw new BadRequestException("Nothing to update!");
        }

        if (gameSell.getComment() != null) {
            gameSell.setComment(gameSell.getComment());
        }
        if (gameSell.getPrice() != null) {
            gameSell.setPrice(gameSell.getPrice());
        }
        if (gameSell.getCondition() != null) {
            gameSell.setCondition(gameSell.getCondition());
        }

        gameSellRepository.save(gameSell);
    }

    public GameSellDTO entityToGameSellDTO(Game game) {
        return new GameSellDTO(gameMapper.gameToGameDTO(game), null, null, null);
    }

    public List<GameSellDTO> projectionsToGameSellDTO(List<GameSellProjection> projections) {
        ArrayList<GameSellDTO> gamesForSell = new ArrayList<>();
        for (var projection :
                projections) {
            gamesForSell.add(new GameSellDTO(gameMapper.gameToGameDTO(projection.getGame()),
                    projection.getCondition(),
                    projection.getComment(),
                    projection.getPrice()
            ));
        }

        return gamesForSell;
    }
}
