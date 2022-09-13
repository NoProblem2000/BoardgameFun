package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.exception.BadRequestException;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.UserWish;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.repository.UserWishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserWishService {

    final UserWishRepository userWishRepository;
    final UserRepository userRepository;
    final GameRepository gameRepository;

    public UserWishService(UserWishRepository userWishRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.userWishRepository = userWishRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public String addGameToUserWishlist(Integer userId, Integer gameId) {
        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        if (userWishRepository.findByUserAndGame(user, game) != null) {
            throw new BadRequestException("user wish already exists");
        }

        if (user == null || game == null) {
            throw new NoRecordFoundException("user or game not found");
        }

        var userWish = new UserWish();
        userWish.setGame(game);
        userWish.setUser(user);

        userWishRepository.save(userWish);
        return game.getTitle();
    }

    @Transactional
    public void deleteGameFromUserWishlist(Integer userWishId) {
        var userWish = userWishRepository.findById(userWishId);

        if (userWish.isEmpty()) {
            throw new NoRecordFoundException("Userwish is not found");
        }
        userWishRepository.delete(userWish.get());
    }
}
