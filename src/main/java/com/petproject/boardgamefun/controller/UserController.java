package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameRatingDTO;
import com.petproject.boardgamefun.model.*;
import com.petproject.boardgamefun.repository.*;
import com.petproject.boardgamefun.security.jwt.JwtUtils;
import com.petproject.boardgamefun.security.model.JwtResponse;
import com.petproject.boardgamefun.security.model.LoginRequest;
import com.petproject.boardgamefun.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    final GameRepository gameRepository;
    final UserRepository userRepository;
    final UserOwnGameRepository userOwnGameRepository;
    final RatingGameByUserRepository ratingGameByUserRepository;
    final UserWishRepository userWishRepository;

    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final AuthenticationManager authenticationManager;

    public UserController(GameRepository gameRepository,
                          UserRepository userRepository,
                          UserOwnGameRepository userOwnGameRepository,
                          RatingGameByUserRepository ratingGameByUserRepository,
                          UserWishRepository userWishRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.userOwnGameRepository = userOwnGameRepository;
        this.ratingGameByUserRepository = ratingGameByUserRepository;
        this.userWishRepository = userWishRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        var users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        if (!userRepository.existsByName(loginRequest.getName())) {
            return new ResponseEntity<>("Пользователя с таким никнеймом не существует", HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        if (userRepository.existsByName(user.getName())) {
            return new ResponseEntity<>("Пользователь с таким никнеймом уже существует", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByMail(user.getMail())) {
            return new ResponseEntity<>("Пользователь с такой почтой уже существует", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(OffsetDateTime.now());
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        var user = userRepository.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/games")
    public ResponseEntity<List<Game>> getUserCollectionByType(@PathVariable Integer id) {

        var games = gameRepository.findUserGames(id);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("{userId}/add-game/{gameId}")
    public ResponseEntity<?> addGameToUser(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        var userOwnGame = new UserOwnGame();
        userOwnGame.setGame(game);
        userOwnGame.setUser(user);

        userOwnGameRepository.save(userOwnGame);

        return new ResponseEntity<>(game.getTitle() + " добавлена в вашу коллекцию", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{userId}/delete-game/{gameId}")
    public ResponseEntity<?> deleteGameFromUserCollection(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        var userOwnGame = userOwnGameRepository.findByGameAndUser(game, user);

        userOwnGameRepository.delete(userOwnGame);

        return new ResponseEntity<>(game.getTitle() + " удалена из вашей коллекции", HttpStatus.OK);
    }

    @GetMapping("/{userId}/games-rating")
    public ResponseEntity<List<GameRatingDTO>> getUserRatingList(@PathVariable Integer userId) {

        var ratingGamesByUser = gameRepository.findGameRatingList(userId);

        return new ResponseEntity<>(ratingGamesByUser, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{userId}/delete-game-rating/{gameId}")
    public ResponseEntity<String> deleteGameRating(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        var ratingGameByUser = ratingGameByUserRepository.findByGameAndUser(game, user);

        ratingGameByUserRepository.delete(ratingGameByUser);

        return new ResponseEntity<>("Оценка с текущей игры удалена", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/{userId}/set-game-rating/{gameId}/{rating}")
    public ResponseEntity<Integer> setGameRating(@PathVariable Integer userId, @PathVariable Integer gameId, @PathVariable Integer rating) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        var ratingGameByUser = ratingGameByUserRepository.findByGameAndUser(game, user);

        if (ratingGameByUser != null) {
            ratingGameByUser.setRating(rating);
            ratingGameByUserRepository.save(ratingGameByUser);
        } else {
            var gameRating = new RatingGameByUser();
            gameRating.setGame(game);
            gameRating.setUser(user);
            gameRating.setRating(rating);

            ratingGameByUserRepository.save(gameRating);
        }

        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @GetMapping("/{id}/wishlist")
    public ResponseEntity<List<Game>> getUserWishlist(@PathVariable Integer id) {
        var games = gameRepository.findUserWishlist(id);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("{userId}/add-game-to-wishlist/{gameId}")
    public ResponseEntity<?> addGameToUserWishlist(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        var userWish = new UserWish();
        userWish.setGame(game);
        userWish.setUser(user);

        userWishRepository.save(userWish);

        return new ResponseEntity<>(game.getTitle() + " добавлена в ваши желания", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{userId}/delete-game-from-wishlist/{gameId}")
    public ResponseEntity<?> deleteGameFromUserWishlist(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        var userWish = userWishRepository.findByGameAndUser(game, user);

        userWishRepository.delete(userWish);

        return new ResponseEntity<>(game.getTitle() + " удалена из вашего списка желаемого", HttpStatus.OK);
    }

    //todo: sell games
    //todo: diary
    //todo: edit user
    //todo: add game

}
