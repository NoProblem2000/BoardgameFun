package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.exception.BadRequestException;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.UserOwnGame;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.UserOwnGameRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserOwnGameService {

    final UserOwnGameRepository userOwnGameRepository;
    final GameRepository gameRepository;
    final UserRepository userRepository;

    public UserOwnGameService(UserOwnGameRepository userOwnGameRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.userOwnGameRepository = userOwnGameRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String addGameToUser(Integer userId, Integer gameId) {
        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        if (user == null || game == null)
            throw new NoRecordFoundException();

        var games = gameRepository.findUserGames(userId);
        if (games.size() != 0 && games.stream().anyMatch(g -> g.getId().equals(gameId))) {
            throw new BadRequestException();
        }

        var userOwnGame = new UserOwnGame();
        userOwnGame.setGame(game);
        userOwnGame.setUser(user);

        userOwnGameRepository.save(userOwnGame);

        return game.getTitle();
    }
    @Transactional
    public void deleteGameFromUserCollection(Integer userId, Integer gameId){
        var userOwnGame = userOwnGameRepository.findUserOwnGame_ByUserIdAndGameId(userId, gameId);

        if (userOwnGame == null)
            throw new NoRecordFoundException();

        userOwnGameRepository.delete(userOwnGame);
    }
}
