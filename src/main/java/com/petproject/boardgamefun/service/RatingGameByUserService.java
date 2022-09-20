package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.RatingGameByUserDTO;
import com.petproject.boardgamefun.exception.BadRequestException;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.RatingGameByUser;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.RatingGameByUserRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RatingGameByUserService {

    final RatingGameByUserRepository ratingGameByUserRepository;
    final GameRepository gameRepository;
    final UserRepository userRepository;
    final GameService gameService;

    public RatingGameByUserService(RatingGameByUserRepository ratingGameByUserRepository, GameRepository gameRepository, UserRepository userRepository, GameService gameService) {
        this.ratingGameByUserRepository = ratingGameByUserRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameService = gameService;
    }

    @Transactional
    public void deleteGameRating(Integer userId, Integer gameId) {
        var ratingGameByUser = ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(userId, gameId);

        if (ratingGameByUser == null)
            throw new NoRecordFoundException();

        ratingGameByUserRepository.delete(ratingGameByUser);
    }

    @Transactional
    public Double setGameRating(Integer userId, Integer gameId, Integer rating) {
        if (rating > 10 || rating < 1) {
            throw new BadRequestException("Invalid rating");
        }
        if (ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(userId, gameId) != null) {
            throw new BadRequestException("Rating already exists");
        }

        var gameRating = new RatingGameByUser();
        var game = gameRepository.findGameById(gameId);
        var user = userRepository.findUserById(userId);

        if (game == null || user == null)
            throw new NoRecordFoundException("Game or user not found");

        gameRating.setGame(game);
        gameRating.setUser(user);
        gameRating.setRating(rating.doubleValue());

        ratingGameByUserRepository.save(gameRating);

        return rating.doubleValue();
    }

    @Transactional
    public Double updateGameRating(Integer userId, Integer gameId, Integer rating) {
        if (rating > 10 || rating < 1) {
            throw new BadRequestException("Invalid rating");
        }
        var ratingGameByUser = ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(userId, gameId);
        if (ratingGameByUser == null) {
            throw new NoRecordFoundException("No rating by user");
        }

        ratingGameByUser.setRating(rating.doubleValue());
        ratingGameByUserRepository.save(ratingGameByUser);
        return rating.doubleValue();
    }

    @Transactional
    public List<RatingGameByUserDTO> getUsersRating(Integer gameId){
        return gameService.usersGameRatingToDTO(userRepository.findGameRatings(gameId));
    }

    public List<GameDataDTO> getUserRatingList(Integer userId){
        return gameService.userGameRatingToGameDTO(gameRepository.findUserGameRatingList(userId));
    }

}
