package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.DiaryDTO;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.dto.request.PasswordChangeRequest;
import com.petproject.boardgamefun.dto.request.UserEditRequest;
import com.petproject.boardgamefun.model.*;
import com.petproject.boardgamefun.repository.*;
import com.petproject.boardgamefun.security.enums.Role;
import com.petproject.boardgamefun.security.jwt.JwtUtils;
import com.petproject.boardgamefun.security.model.JwtResponse;
import com.petproject.boardgamefun.security.model.LoginRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenResponse;
import com.petproject.boardgamefun.security.services.RefreshTokenService;
import com.petproject.boardgamefun.security.services.UserDetailsImpl;
import com.petproject.boardgamefun.service.DiaryService;
import com.petproject.boardgamefun.service.GameSellService;
import com.petproject.boardgamefun.service.GameService;
import com.petproject.boardgamefun.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    final GameRepository gameRepository;
    final UserRepository userRepository;
    final UserOwnGameRepository userOwnGameRepository;
    final RatingGameByUserRepository ratingGameByUserRepository;
    final UserWishRepository userWishRepository;
    final GameSellRepository gameSellRepository;
    final DiaryRepository diaryRepository;
    final DiaryService diaryService;
    final GameSellService gameSellService;
    final GameService gameService;
    final UserService userService;


    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final AuthenticationManager authenticationManager;
    final RefreshTokenService refreshTokenService;

    public UserController(GameRepository gameRepository,
                          UserRepository userRepository,
                          UserOwnGameRepository userOwnGameRepository,
                          RatingGameByUserRepository ratingGameByUserRepository,
                          UserWishRepository userWishRepository,
                          GameSellRepository gameSellRepository, DiaryRepository diaryRepository, DiaryService diaryService, GameSellService gameSellService, GameService gameService, UserService userService, PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.userOwnGameRepository = userOwnGameRepository;
        this.ratingGameByUserRepository = ratingGameByUserRepository;
        this.userWishRepository = userWishRepository;
        this.gameSellRepository = gameSellRepository;
        this.diaryRepository = diaryRepository;
        this.diaryService = diaryService;
        this.gameSellService = gameSellService;
        this.gameService = gameService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        var users = userService.entitiesToUserDTO(userRepository.findAll());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        if (!userRepository.existsByName(loginRequest.getName())) {
            return new ResponseEntity<>("Пользователя с таким никнеймом не существует", HttpStatus.NOT_FOUND);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getName());
        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), refreshToken), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        if (userRepository.existsByName(user.getName())) {
            return new ResponseEntity<>("Пользователь с таким никнеймом уже существует", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByMail(user.getMail())) {
            return new ResponseEntity<>("Пользователь с такой почтой уже существует", HttpStatus.BAD_REQUEST);
        }

        user.setRole(Role.ROLE_USER.name());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(OffsetDateTime.now());
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshTokenService.verifyExpiration(refreshToken)) {
            var token = jwtUtils.generateJwtToken(refreshTokenRequest.getUserName());
            return new ResponseEntity<>(new RefreshTokenResponse(token, refreshToken), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Transactional
    @PostMapping("/upload-avatar/{userName}")
    public ResponseEntity<String> uploadAvatar(@PathVariable String userName, @RequestParam("avatar") MultipartFile file) throws IOException {
        var user = userRepository.findUserByName(userName);
        user.setAvatar(file.getBytes());
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Transactional
    @PatchMapping("/edit/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> editUser(@PathVariable Integer userId, @RequestBody UserEditRequest userEditRequest) {
        var user = userRepository.findUserById(userId);
        if (userEditRequest.getName() != null && !userRepository.existsByName(userEditRequest.getName())) {
            user.setName(userEditRequest.getName());
        } else {
            return new ResponseEntity<>("Пользователь с таким никнеймом уже существует", HttpStatus.BAD_REQUEST);
        }
        if (userEditRequest.getRole() != null && !Objects.equals(userEditRequest.getRole(), user.getRole())) {
            user.setRole(userEditRequest.getRole());
        }

        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/change-password/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Integer userId, @RequestBody PasswordChangeRequest passwordRequest) {
        var user = userRepository.findUserById(userId);
        if (passwordRequest.getPassword() != null && passwordEncoder.matches(passwordRequest.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        } else {
            return new ResponseEntity<>("Вы ввели точно такой же пароль", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Transactional
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        var userDTO = userService.entityToUserDTO(userRepository.findUserById(id));
        if (userDTO.getUser() == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{id}/games")
    public ResponseEntity<List<GameDTO>> getUserCollection(@PathVariable Integer id) {

        var games = gameService.entitiesToGameDTO(gameRepository.findUserGames(id));
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("{userId}/add-game/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addGameToUser(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        if (user == null || game == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        var games = gameRepository.findUserGames(userId);
        if (games.size() != 0 && games.stream().anyMatch(g -> g.getId().equals(gameId))) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        var userOwnGame = new UserOwnGame();
        userOwnGame.setGame(game);
        userOwnGame.setUser(user);

        userOwnGameRepository.save(userOwnGame);

        return new ResponseEntity<>(game.getTitle() + " добавлена в вашу коллекцию", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{userId}/delete-game/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteGameFromUserCollection(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var userOwnGame = userOwnGameRepository.findUserOwnGame_ByUserIdAndGameId(userId, gameId);

        if (userOwnGame == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        userOwnGameRepository.delete(userOwnGame);

        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @GetMapping("/{userId}/games-rating")
    public ResponseEntity<List<GameDTO>> getUserRatingList(@PathVariable Integer userId) {

        var ratingGamesByUser = gameService.userGameRatingToGameDTO(gameRepository.findUserGameRatingList(userId));

        return new ResponseEntity<>(ratingGamesByUser, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{userId}/delete-game-rating/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteGameRating(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var ratingGameByUser = ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(userId, gameId);

        if (ratingGameByUser == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        ratingGameByUserRepository.delete(ratingGameByUser);

        return new ResponseEntity<>("Оценка с текущей игры удалена", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/{userId}/set-game-rating/{gameId}/{rating}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> setGameRating(@PathVariable Integer userId, @PathVariable Integer gameId, @PathVariable Integer rating) {

        if (rating > 10 || rating < 1) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(userId, gameId) != null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        var gameRating = new RatingGameByUser();
        var game = gameRepository.findGameById(gameId);
        var user = userRepository.findUserById(userId);

        if (game == null || user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        gameRating.setGame(game);
        gameRating.setUser(user);
        gameRating.setRating(rating);

        ratingGameByUserRepository.save(gameRating);


        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/{userId}/update-game-rating/{gameId}/{rating}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> updateGameRating(@PathVariable Integer userId, @PathVariable Integer gameId, @PathVariable Integer rating) {

        if (rating > 10 || rating < 1) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        var ratingGameByUser = ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(userId, gameId);
        if (ratingGameByUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ratingGameByUser.setRating(rating);
        ratingGameByUserRepository.save(ratingGameByUser);

        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @GetMapping("/{id}/wishlist")
    public ResponseEntity<List<GameDTO>> getUserWishlist(@PathVariable Integer id) {
        var games = gameService.entitiesToGameDTO(gameRepository.findUserWishlist(id));
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("{userId}/add-game-to-wishlist/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addGameToUserWishlist(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        if (userWishRepository.findByUserAndGame(user, game) != null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (user == null || game == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        var userWish = new UserWish();
        userWish.setGame(game);
        userWish.setUser(user);

        userWishRepository.save(userWish);

        return new ResponseEntity<>(game.getTitle() + " добавлена в ваши желания", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("delete-game-from-wishlist/{userWishId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteGameFromUserWishlist(@PathVariable Integer userWishId) {

        var userWish = userWishRepository.findById(userWishId);

        if (userWish.isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        userWishRepository.delete(userWish.get());

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("{userId}/add-game-to-sell/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<GameSellDTO>> addGameToSellList(@PathVariable Integer userId, @PathVariable Integer gameId, @RequestBody GameSell gameSell) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        gameSell.setGame(game);
        gameSell.setUser(user);

        gameSellRepository.save(gameSell);
        var gameSellList = gameSellService.projectionsToGameSellDTO(gameRepository.getGameSellList(userId));

        return new ResponseEntity<>(gameSellList, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("{userId}/games-to-sell")
    public ResponseEntity<List<GameSellDTO>> getGameSellList(@PathVariable Integer userId) {

        var gameSellList = gameSellService.projectionsToGameSellDTO(gameRepository.getGameSellList(userId));

        return new ResponseEntity<>(gameSellList, HttpStatus.OK);
    }


    @Transactional
    @DeleteMapping("{userId}/remove-game-from-sell/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> removeGameFromSell(@PathVariable Integer userId, @PathVariable Integer gameId) {

        var gameSell = gameSellRepository.findGameSell_ByGameIdAndUserId(gameId, userId);
        gameSellRepository.delete(gameSell);

        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/update-game-to-sell")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> updateSellGame(@RequestBody GameSell gameSell) {

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

        return new ResponseEntity<>(gameSell.getGame().getTitle() + " обновлена", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("{userId}/add-diary/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryDTO> addDiary(@PathVariable Integer userId, @PathVariable Integer gameId, @RequestBody Diary diary) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        diary.setUser(user);
        diary.setGame(game);
        diary.setPublicationTime(OffsetDateTime.now());
        diaryRepository.save(diary);

        var diaryDTO = diaryService.entityToDiaryDTO(diary);

        return new ResponseEntity<>(diaryDTO, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{userId}/remove-diary/{diaryId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteDiary(@PathVariable Integer userId, @PathVariable Integer diaryId) {

        var diary = diaryRepository.findDiary_ByUserIdAndId(userId, diaryId);

        diaryRepository.delete(diary);

        return new ResponseEntity<>("Дневник " + diary.getTitle() + " удален из ваших дневников", HttpStatus.OK);
    }

    @Transactional
    @PutMapping({"{userId}/update-diary/{diaryId}"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DiaryDTO> updateDiary(@PathVariable Integer diaryId, @PathVariable Integer userId, @RequestBody Diary diaryRequest) {

        var diary = diaryRepository.findDiary_ByUserIdAndId(userId, diaryId);
        if (diaryRequest.getTitle() != null && !Objects.equals(diary.getTitle(), diaryRequest.getTitle())) {
            diary.setTitle(diaryRequest.getTitle());
        }
        if (diaryRequest.getText() != null && !Objects.equals(diary.getText(), diaryRequest.getText())) {
            diary.setText(diaryRequest.getText());
        }

        diaryRepository.save(diary);

        var diaryDTO = diaryService.entityToDiaryDTO(diary);

        return new ResponseEntity<>(diaryDTO, HttpStatus.OK);
    }

    //todo: optimize response - not whole model, only needed fields
    //todo: add news
    //todo: unique repository???

}
