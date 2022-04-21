package com.petproject.boardgamefun;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.dto.projection.GameSellProjection;
import com.petproject.boardgamefun.dto.projection.UserGameRatingProjection;
import com.petproject.boardgamefun.model.*;

import com.petproject.boardgamefun.repository.*;
import com.petproject.boardgamefun.security.jwt.JwtUtils;
import com.petproject.boardgamefun.security.model.LoginRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenResponse;
import com.petproject.boardgamefun.security.services.RefreshTokenService;
import com.petproject.boardgamefun.security.services.UserDetailsImpl;
import com.petproject.boardgamefun.service.GameSellService;
import com.petproject.boardgamefun.service.GameService;
import com.petproject.boardgamefun.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringSecurityWebTestConfig.class
)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTests {

    private final String Gateway = "/users";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private RatingGameByUserRepository ratingGameByUserRepository;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Authentication authentication;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsImpl userDetails;

    @MockBean
    private UserOwnGameRepository userOwnGameRepository;

    @MockBean
    private UserWishRepository userWishRepository;

    @MockBean
    private GameSellRepository gameSellRepository;

    @MockBean
    private GameSellService gameSellService;

    private List<UserDTO> usersDTO;
    private UserDTO userDTO;
    private User userAdmin;
    private User userModerator;
    private User user;
    private List<User> users;
    private List<GameDTO> gamesDTO;
    private GameDTO gameDTO;
    private Game game;
    private List<Game> games;
    private List<UserGameRatingProjection> userGameRatingProjections;
    private Designer designer;
    private DesignerDTO designerDTO;
    private RatingGameByUser ratingGameByUser;
    private UserOwnGame userOwnGame;
    private UserWish userWish;
    private List<GameSellProjection> gameSellProjectionList;
    private List<GameSellDTO> gameSellDTOList;
    private GameSellDTO gameSellDTO;
    private GameSell gameSell;

    @Autowired
    private MockMvc mockMvc;
    private static final int insertDataOrder = 1, checkOnDuplicate = 2, updateDataOrder = 3, deleteDataOrder = 4;

    @Captor
    ArgumentCaptor<List<User>> userListArgumentCaptor;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    ArgumentCaptor<List<Game>> gameListArgumentCaptor;

    @Captor
    ArgumentCaptor<List<UserGameRatingProjection>> userGameRatingProjectionCaptor;

    @Captor
    ArgumentCaptor<Game> gameArgumentCaptor;

    @Captor
    ArgumentCaptor<RatingGameByUser> ratingGameByUserArgumentCaptor;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    ObjectMapper objectMapper;

    User admin;

    @BeforeAll
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        userAdmin = new User();
        userAdmin.setId(11);
        userAdmin.setName("Alice");
        userAdmin.setPassword("pswd");
        userAdmin.setRole("ADMIN");
        userAdmin.setMail("alicealisson@ggogle.com");
        userAdmin.setRating(1.0);
        userAdmin.setRegistrationDate(OffsetDateTime.now());

        userModerator = new User();
        userModerator.setId(14);
        userModerator.setName("Sam");
        userModerator.setPassword("pswdsam");
        userModerator.setRole("MODERATOR");
        userModerator.setMail("samwinchester@ggogle.com");
        userModerator.setRating(1.0);
        userModerator.setRegistrationDate(OffsetDateTime.now());

        user = new User();
        user.setId(34);
        user.setName("Bobby");
        user.setPassword("1234qwer");
        user.setRole("USER");
        user.setMail("bobby@ggogle.com");
        user.setRating(1.0);
        user.setRegistrationDate(OffsetDateTime.now());

        users = new ArrayList<>();
        users.add(userAdmin);
        users.add(userModerator);
        users.add(user);


        userDTO = new UserDTO(user);
        usersDTO = new ArrayList<>();
        usersDTO.add(new UserDTO(userAdmin));
        usersDTO.add(new UserDTO(userModerator));
        usersDTO.add(new UserDTO(user));


        game = new Game();
        game.setId(1);
        game.setTitle(" Игра номер 1");
        game.setDescription("Отличная игра войнушка номер 1");
        game.setAnnotation("Отличная игра номер 1");
        game.setPicture(null);
        game.setPlayerAge("14");
        game.setPlayersMin(3);
        game.setPlayersMax(5);
        game.setTimeToPlayMin(120);
        game.setTimeToPlayMax(360);
        game.setYearOfRelease(OffsetDateTime.now());


        Game game1 = new Game();
        game1.setId(1);
        game1.setTitle(" Игра номер 2");
        game1.setDescription("Отличная игра войнушка номер 2");
        game1.setAnnotation("Отличная игра номер 2");
        game1.setPicture(null);
        game1.setPlayerAge("16");
        game1.setPlayersMin(2);
        game1.setPlayersMax(4);
        game1.setTimeToPlayMin(30);
        game1.setTimeToPlayMax(120);
        game1.setYearOfRelease(OffsetDateTime.now());

        games = new ArrayList<>();
        games.add(game);
        games.add(game1);

        designer = new Designer();
        designer.setId(1);
        designer.setName("Designer number one");

        Designer designer2 = new Designer();
        designer2.setId(2);
        designer2.setName("Designer number two");

        List<String> designers = new ArrayList<>();
        designers.add(designer.getName());
        designers.add(designer2.getName());

        gameDTO = new GameDTO(game, 8.4, designers);
        designers.remove(1);
        GameDTO gameDTO1 = new GameDTO(game, 7.9, designers);

        userGameRatingProjections = new ArrayList<>();
        userGameRatingProjections.add(new UserGameRatingPOJO(game, 3));
        userGameRatingProjections.add(new UserGameRatingPOJO(game, 10));

        gamesDTO = new ArrayList<>();
        gamesDTO.add(gameDTO);
        gamesDTO.add(gameDTO1);

        ratingGameByUser = new RatingGameByUser();
        ratingGameByUser.setUser(user);
        ratingGameByUser.setGame(game);
        ratingGameByUser.setRating(10);

        userOwnGame = new UserOwnGame();
        userOwnGame.setUser(user);
        userOwnGame.setGame(game);

        userWish = new UserWish();
        userWish.setUser(user);
        userWish.setGame(game);
        userWish.setId(1);

        gameSell = new GameSell();
        gameSell.setGame(game);
        gameSell.setCondition("good");
        gameSell.setComment("so good");
        gameSell.setPrice(100);
        gameSell.setUser(user);

        gameSellProjectionList = new ArrayList<>();
        gameSellProjectionList.add(new GameSellPOJO(game, "good", "so good", 100));
        gameSellProjectionList.add(new GameSellPOJO(game1, "excellent", "not open", 300));

        gameSellDTO = new GameSellDTO();
        gameSellDTO.setGame(game);
        gameSellDTO.setCondition("good");
        gameSellDTO.setComment("so good");
        gameSellDTO.setPrice(100);

        gameSellDTOList = new ArrayList<>();
        gameSellDTOList.add(gameSellDTO);
        gameSellDTOList.add(new GameSellDTO(game1, "excellent", "not open", 300));

    }

    @Test
    public void getUsersShouldReturnOk() throws Exception {
        when(userRepository.findAll()).thenReturn(users);
        when(userService.entitiesToUserDTO(users)).thenReturn(usersDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway)).andDo(print()).andExpect(status().isOk());


        verify(userRepository, only()).findAll();
        verify(userService, only()).entitiesToUserDTO(userListArgumentCaptor.capture());
        Assertions.assertEquals(userListArgumentCaptor.getValue().size(), 3);
    }

    @Test
    public void getUsersShouldReturnOk_BlankList() throws Exception {
        when(userRepository.findAll()).thenReturn(null);
        when(userService.entitiesToUserDTO(new ArrayList<>())).thenReturn(new ArrayList<>());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway)).andDo(print()).andExpect(status().isOk()).andReturn();
        UserDTO[] userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserDTO[].class);
        verify(userRepository, only()).findAll();
        verify(userService, only()).entitiesToUserDTO(userListArgumentCaptor.capture());
        Assertions.assertNull(userListArgumentCaptor.getValue());
        Assertions.assertEquals(userResponse.length, 0);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void authenticateUserShouldReturnOk() throws Exception {
        LoginRequest loginRequest = new LoginRequest(user.getName(), user.getPassword());

        when(userRepository.existsByName(user.getName())).thenReturn(true);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()))).thenReturn(authentication);
        when(userRepository.findUserByName(user.getName())).thenReturn(user);
        when(refreshTokenService.createRefreshToken(user.getName())).thenReturn("token");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("124");
        when(userDetails.getId()).thenReturn(user.getId());
        when(userDetails.getUsername()).thenReturn(user.getName());
        when(userDetails.getEmail()).thenReturn(user.getMail());

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/sign-in").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk());

        verify(userRepository).existsByName(user.getName());
        verify(refreshTokenService).createRefreshToken(stringArgumentCaptor.capture());
        Assertions.assertEquals(stringArgumentCaptor.getValue(), user.getName());
    }

    @Test
    public void authenticateUserShouldReturn415() throws Exception {
        LoginRequest loginRequest = new LoginRequest("Admin", "123qweAdmin");
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/sign-in")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void authenticateUserShouldReturnNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest("-1Admin", "123qweAdmin");
        when(userRepository.existsByName(user.getName())).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isNotFound());
    }

    @Test
    public void authenticateUserShouldReturnNotAuthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest(user.getName(), user.getPassword());

        when(userRepository.existsByName(user.getName())).thenReturn(true);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()))).thenThrow(new AuthenticationException("auth failed") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        });

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
    }

    @Test
    public void authenticateUserShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void refreshTokenShouldReturnNotAuthorizedBadAccessToken() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("Admin", "bla-bla");
        when(refreshTokenService.verifyExpiration(refreshTokenRequest.getRefreshToken())).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isUnauthorized());
        verify(refreshTokenService, only()).verifyExpiration(refreshTokenRequest.getRefreshToken());
    }

    @Test
    public void refreshTokenShouldReturnIsOk() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("Admin", "bla-bla");

        when(refreshTokenService.verifyExpiration(refreshTokenRequest.getRefreshToken())).thenReturn(true);
        when(jwtUtils.generateJwtToken(refreshTokenRequest.getUserName())).thenReturn("new token");

        var response = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk()).andReturn();

        RefreshTokenResponse refreshTokenResponse = objectMapper.readValue(response.getResponse().getContentAsByteArray(), RefreshTokenResponse.class);

        verify(refreshTokenService, only()).verifyExpiration(refreshTokenRequest.getRefreshToken());
        verify(jwtUtils, only()).generateJwtToken(refreshTokenRequest.getUserName());
        Assertions.assertEquals(refreshTokenResponse.getAccessToken(), "new token");
    }

    @Test
    public void getUserShouldReturnStatusOkTest() throws Exception {
        when(userRepository.findUserById(1)).thenReturn(user);
        when(userService.entityToUserDTO(user)).thenReturn(userDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1")).andDo(print()).andExpect(status().isOk());
        verify(userRepository, only()).findUserById(1);
        verify(userService, only()).entityToUserDTO(userArgumentCaptor.capture());
        Assertions.assertEquals(userArgumentCaptor.getValue().getName(), userDTO.getUser().getName());
    }

    @Test
    public void getUserShouldReturnStatusNotFound() throws Exception {
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(userService.entityToUserDTO(null)).thenReturn(new UserDTO());
        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/-1")).andDo(print()).andExpect(status().isNotFound());
        verify(userRepository, only()).findUserById(-1);
        verify(userService, only()).entityToUserDTO(null);
    }

    @Test
    public void getUserWhenBadPathValueReturn400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/{userId}", "userId")).andExpect(status().isBadRequest());
    }

    @Test
    public void getUserWhenValidInput_thenReturnUserResource() throws Exception {

        when(userRepository.findUserById(1)).thenReturn(user);
        when(userService.entityToUserDTO(user)).thenReturn(userDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/{userId}", "1")).andExpect(status().isOk()).andReturn();

        UserDTO userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserDTO.class);

        verify(userRepository, only()).findUserById(1);
        verify(userService, only()).entityToUserDTO(userArgumentCaptor.capture());

        Assertions.assertEquals(userArgumentCaptor.getValue().getName(), userDTO.getUser().getName());

        Assertions.assertEquals(userResponse.getUser().getName(), user.getName());
        Assertions.assertEquals(userResponse.getUser().getMail(), user.getMail());
        Assertions.assertEquals(userResponse.getUser().getRole(), user.getRole());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserCollectionShouldReturnIsOk() throws Exception {
        when(gameRepository.findUserGames(1)).thenReturn(games);
        when(gameService.entitiesToGameDTO(games)).thenReturn(gamesDTO);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1/games")).andExpect(status().isOk()).andReturn();
        var userCollection = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);

        verify(gameRepository, only()).findUserGames(1);
        verify(gameService, only()).entitiesToGameDTO(gameListArgumentCaptor.capture());

        Assertions.assertNotEquals(gameListArgumentCaptor.getValue().size(), 0);
        Assertions.assertEquals(userCollection[0].getGame().getId(), gamesDTO.get(0).getGame().getId());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserCollectionShouldReturnBlankArray() throws Exception {
        when(gameRepository.findUserGames(-1)).thenReturn(null);
        when(gameService.entitiesToGameDTO(new ArrayList<>())).thenReturn(new ArrayList<>());
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/-1/games")).andExpect(status().isOk()).andReturn();
        var gameDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);

        verify(gameRepository, only()).findUserGames(-1);
        verify(gameService, only()).entitiesToGameDTO(gameListArgumentCaptor.capture());

        Assertions.assertNull(gameListArgumentCaptor.getValue());
        Assertions.assertEquals(gameDTOS.length, 0);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserRatingListShouldReturnIsOk() throws Exception {
        when(gameRepository.findUserGameRatingList(1)).thenReturn(userGameRatingProjections);
        when(gameService.userGameRatingToGameDTO(userGameRatingProjections)).thenReturn(gamesDTO);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1/games-rating").characterEncoding("UTF-8"))
                .andExpect(status().isOk()).andReturn();
        var userRatingList = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);

        verify(gameRepository, only()).findUserGameRatingList(1);
        verify(gameService, only()).userGameRatingToGameDTO(userGameRatingProjectionCaptor.capture());

        Assertions.assertNotEquals(userGameRatingProjectionCaptor.getValue().size(), 0);
        Assertions.assertEquals(userRatingList[0].getGame().getId(), gamesDTO.get(0).getGame().getId());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserRatingListShouldReturnBlankArray() throws Exception {
        when(gameRepository.findUserGameRatingList(-1)).thenReturn(null);
        when(gameService.userGameRatingToGameDTO(new ArrayList<>())).thenReturn(new ArrayList<>());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/-1/games-rating")).andExpect(status().isOk()).andReturn();
        var gameDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);

        verify(gameRepository, only()).findUserGameRatingList(-1);
        verify(gameService, only()).userGameRatingToGameDTO(userGameRatingProjectionCaptor.capture());

        Assertions.assertNull(userGameRatingProjectionCaptor.getValue());
        Assertions.assertEquals(gameDTOS.length, 0);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameRatingShouldReturnNotFound_FirstParameter() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(-1, 1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/-1/delete-game-rating/1")).andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository, only()).findRatingGame_ByUserIdAndGameId(-1, 1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameRatingShouldReturnNotFound_SecondParameter() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/delete-game-rating/-1")).andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository, only()).findRatingGame_ByUserIdAndGameId(1, -1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameRatingShouldReturnNotFound_BothParameters() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(-1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/-1/delete-game-rating/-1")).andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository, only()).findRatingGame_ByUserIdAndGameId(-1, -1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameRatingShouldReturnOk() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(1, 1)).thenReturn(ratingGameByUser);
        doNothing().when(ratingGameByUserRepository).delete(ratingGameByUser);

        var mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/delete-game-rating/1")
                .characterEncoding("UTF-8")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(1, 1);
        verify(ratingGameByUserRepository).delete(ratingGameByUserArgumentCaptor.capture());

        Assertions.assertEquals(ratingGameByUserArgumentCaptor.getValue().getId(), ratingGameByUser.getId());
        Assertions.assertEquals(mvcResult, "Оценка с текущей игры удалена");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnOk() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(1, 1)).thenReturn(null);
        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(ratingGameByUserRepository.save(any(RatingGameByUser.class))).thenReturn(null);

        var mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/set-game-rating/1/10"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        var rating = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Integer.class);

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(1, 1);
        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(1);
        verify(ratingGameByUserRepository).save(any(RatingGameByUser.class));
        Assertions.assertEquals(ratingGameByUser.getRating(), rating);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnBadRequest() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(1, 1)).thenReturn(ratingGameByUser);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/set-game-rating/1/10")).andDo(print()).andExpect(status().isBadRequest());

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(1, 1);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnBadRequestLessThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/set-game-rating/1/0")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnBadRequestMoreThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/set-game-rating/1/11")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnNotFound_FirstParameter() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(-1, 1)).thenReturn(null);
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(1)).thenReturn(game);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/set-game-rating/1/10")).andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(-1, 1);
        verify(userRepository).findUserById(-1);
        verify(gameRepository).findGameById(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnNotFound_SecondParameter() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(1, -1)).thenReturn(null);
        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/set-game-rating/-1/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(1, -1);
        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(-1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnNotFound_BothParameters() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(-1, -1)).thenReturn(null);
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/set-game-rating/-1/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(-1, -1);
        verify(userRepository).findUserById(-1);
        verify(gameRepository).findGameById(-1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnOk() throws Exception {

        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(1, 1)).thenReturn(ratingGameByUser);
        when(ratingGameByUserRepository.save(ratingGameByUser)).thenReturn(null);

        var result = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/update-game-rating/1/10"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        var rating = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Integer.class);

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(1, 1);
        verify(ratingGameByUserRepository).save(ratingGameByUser);
        Assertions.assertEquals(ratingGameByUser.getRating(), rating);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnBadRequestLessThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/update-game-rating/1/0")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnBadRequestMoreThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/update-game-rating/1/11")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnNotFound_FirstParameter() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(-1, 1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/update-game-rating/1/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(-1, 1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnNotFound_SecondParameter() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/update-game-rating/-1/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(1, -1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnNotFound_BothParameters() throws Exception {
        when(ratingGameByUserRepository.findRatingGame_ByUserIdAndGameId(-1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/update-game-rating/-1/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(ratingGameByUserRepository).findRatingGame_ByUserIdAndGameId(-1, -1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnOk() throws Exception {

        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(gameRepository.findUserGames(1)).thenReturn(new ArrayList<>());
        when(userOwnGameRepository.save(any(UserOwnGame.class))).thenReturn(userOwnGame);

        var result = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game/1"))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(1);
        verify(gameRepository).findUserGames(1);
        verify(userOwnGameRepository).save(any(UserOwnGame.class));

        Assertions.assertNotEquals(0, result.length());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnBadRequest() throws Exception {
        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(gameRepository.findUserGames(1)).thenReturn(games);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game/1"))
                .andDo(print()).andExpect(status().isBadRequest());

        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(1);
        verify(gameRepository).findUserGames(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusNotFound_FirstParameter() throws Exception {
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(1)).thenReturn(game);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/add-game/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userRepository).findUserById(-1);
        verify(gameRepository).findGameById(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusNotFound_SecondParameter() throws Exception {
        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(-1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusNotFound_BothParameters() throws Exception {
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/add-game/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userRepository).findUserById(-1);
        verify(gameRepository).findGameById(-1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusBadRequest_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/bla/add-game/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusBadRequest_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusBadRequest_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/bla/add-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnNotFound_FirstParameter() throws Exception {
        when(userOwnGameRepository.findUserOwnGame_ByUserIdAndGameId(-1, 1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/-1/delete-game/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userOwnGameRepository).findUserOwnGame_ByUserIdAndGameId(-1, 1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnNotFound_SecondParameter() throws Exception {
        when(userOwnGameRepository.findUserOwnGame_ByUserIdAndGameId(1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/delete-game/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userOwnGameRepository).findUserOwnGame_ByUserIdAndGameId(1, -1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnNotFound_BothParameters() throws Exception {
        when(userOwnGameRepository.findUserOwnGame_ByUserIdAndGameId(-1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/-1/delete-game/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userOwnGameRepository).findUserOwnGame_ByUserIdAndGameId(-1, -1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnBadRequest_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/bla/delete-game/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnBadRequest_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/delete-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnBadRequest_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/bla/delete-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnOk() throws Exception {
        when(userOwnGameRepository.findUserOwnGame_ByUserIdAndGameId(1, 1)).thenReturn(userOwnGame);
        doNothing().when(userOwnGameRepository).delete(userOwnGame);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/delete-game/1"))
                .andDo(print()).andExpect(status().isOk());

        verify(userOwnGameRepository).findUserOwnGame_ByUserIdAndGameId(1, 1);
        verify(userOwnGameRepository).delete(userOwnGame);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserWishlistShouldReturnIsOk() throws Exception {
        when(gameRepository.findUserWishlist(1)).thenReturn(games);
        when(gameService.entitiesToGameDTO(games)).thenReturn(gamesDTO);

        var mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1/wishlist"))
                .andExpect(status().isOk()).andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);

        verify(gameRepository).findUserWishlist(1);
        verify(gameService).entitiesToGameDTO(gameListArgumentCaptor.capture());

        Assertions.assertEquals(response.length, gamesDTO.size());
        Assertions.assertEquals(response[0].getGame().getId(), gamesDTO.get(0).getGame().getId());
        Assertions.assertEquals(response[0].getRating(), gamesDTO.get(0).getRating());
        Assertions.assertEquals(gameListArgumentCaptor.getValue().size(), games.size());

    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserWishlistShouldReturnBlankArray() throws Exception {
        when(gameRepository.findUserWishlist(-1)).thenReturn(new ArrayList<>());
        when(gameService.entitiesToGameDTO(new ArrayList<>())).thenReturn(new ArrayList<>());
        var mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/-1/wishlist")).andExpect(status().isOk()).andReturn();
        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);
        Assertions.assertEquals(0, response.length);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserWishlistShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/bla/wishlist")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnOk() throws Exception {
        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(userWishRepository.findByUserAndGame(user, game)).thenReturn(null);
        when(userWishRepository.save(any(UserWish.class))).thenReturn(userWish);

        var response = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game-to-wishlist/1"))
                .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(1);
        verify(userWishRepository).findByUserAndGame(user, game);
        verify(userWishRepository).save(any(UserWish.class));

        Assertions.assertNotEquals(response.length(), 0);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnBadRequest_CheckOnDuplicates() throws Exception {
        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(userWishRepository.findByUserAndGame(user, game)).thenReturn(userWish);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game-to-wishlist/1"))
                .andDo(print()).andExpect(status().isBadRequest());

        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(1);
        verify(userWishRepository).findByUserAndGame(user, game);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusNotFound_FirstParameter() throws Exception {
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(1)).thenReturn(game);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/add-game-to-wishlist/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userRepository).findUserById(-1);
        verify(gameRepository).findGameById(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusNotFound_SecondParameter() throws Exception {
        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game-to-wishlist/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(-1)).thenReturn(null);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusNotFound_BothParameters() throws Exception {
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/add-game-to-wishlist/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        when(userRepository.findUserById(-1)).thenReturn(user);
        when(gameRepository.findGameById(-1)).thenReturn(null);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusBadRequest_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/bla/add-game-to-wishlist/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusBadRequest_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game-to-wishlist/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusBadRequest_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/bla/add-game-to-wishlist/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserWishlistShouldReturnOk() throws Exception {
        when(userWishRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(userWish));
        doNothing().when(userWishRepository).delete(userWish);

        var result = this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/delete-game-from-wishlist/1"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), boolean.class);

        verify(userWishRepository).findById(1);
        verify(userWishRepository).delete(userWish);

        Assertions.assertEquals(true, response);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserWishlistShouldReturnNotFound() throws Exception {
        when(userWishRepository.findById(1)).thenReturn(Optional.empty());

        var result = this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/delete-game-from-wishlist/1"))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), boolean.class);

        verify(userWishRepository).findById(1);

        Assertions.assertEquals(false, response);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserWishlistShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/delete-game-from-wishlist/bla-bla"))
                .andDo(print()).andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnOk() throws Exception {

        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(gameSellRepository.save(any(GameSell.class))).thenReturn(null);
        when(gameRepository.getGameSellList(1)).thenReturn(gameSellProjectionList);
        when(gameSellService.projectionsToGameSellDTO(gameSellProjectionList)).thenReturn(gameSellDTOList);

        var mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game-to-sell/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameSell)))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        var response = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameSellDTO[].class);

        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(1);
        verify(gameSellRepository).save(any(GameSell.class));
        verify(gameRepository).getGameSellList(1);
        verify(gameSellService).projectionsToGameSellDTO(gameSellProjectionList);

        Assertions.assertEquals(response.length, gameSellDTOList.size());
        Assertions.assertEquals(response[0].getGame().getId(), gameSellDTOList.get(0).getGame().getId());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnBadRequest_Duplicates() throws Exception {

        gameSell.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game-to-sell/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameSell)))
                .andDo(print()).andExpect(status().isBadRequest());
        gameSell.setId(null);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnNotFound_FirstParameter() throws Exception {

        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(1)).thenReturn(game);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/add-game-to-sell/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameSell)))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userRepository).findUserById(-1);
        verify(gameRepository).findGameById(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnNotFound_SecondParameter() throws Exception {

        when(userRepository.findUserById(1)).thenReturn(user);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/add-game-to-sell/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameSell)))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userRepository).findUserById(1);
        verify(gameRepository).findGameById(-1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnNotFound_BothParameters() throws Exception {

        when(userRepository.findUserById(-1)).thenReturn(null);
        when(gameRepository.findGameById(-1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/add-game-to-sell/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameSell)))
                .andDo(print()).andExpect(status().isNotFound());

        verify(userRepository).findUserById(-1);
        verify(gameRepository).findGameById(-1);
    }

    @Test
    public void getGameSellListShouldReturnOk() throws Exception {

        when(gameRepository.getGameSellList(1)).thenReturn(gameSellProjectionList);
        when(gameSellService.projectionsToGameSellDTO(gameSellProjectionList)).thenReturn(gameSellDTOList);

        var result = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1/games-to-sell"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), GameSellDTO[].class);

        verify(gameRepository).getGameSellList(1);
        verify(gameSellService).projectionsToGameSellDTO(gameSellProjectionList);

        Assertions.assertEquals(response.length, gameSellDTOList.size());
        Assertions.assertEquals(response[0].getGame().getId(), gameSellDTOList.get(0).getGame().getId());

    }

    @Test
    public void getGameSellListShouldReturnBlankArray() throws Exception {

        when(gameRepository.getGameSellList(1)).thenReturn(new ArrayList<>());
        when(gameSellService.projectionsToGameSellDTO(new ArrayList<>())).thenReturn(new ArrayList<>());

        var result = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1/games-to-sell"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), GameSellDTO[].class);

        verify(gameRepository).getGameSellList(1);
        verify(gameSellService).projectionsToGameSellDTO(new ArrayList<>());

        Assertions.assertEquals(response.length, 0);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeGameFromSellShouldReturnOk() throws Exception {
        when(gameSellRepository.findGameSell_ByUserIdAndGameId(1, 1)).thenReturn(gameSell);
        doNothing().when(gameSellRepository).delete(gameSell);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/remove-game-from-sell/1"))
                .andDo(print()).andExpect(status().isOk());

        verify(gameSellRepository).findGameSell_ByUserIdAndGameId(1, 1);
        verify(gameSellRepository).delete(gameSell);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeGameFromSellShouldReturnNotFound_FirstParameter() throws Exception {
        when(gameSellRepository.findGameSell_ByUserIdAndGameId(-1, 1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/-1/remove-game-from-sell/1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(gameSellRepository).findGameSell_ByUserIdAndGameId(-1, 1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeGameFromSellShouldReturnNotFound_SecondParameter() throws Exception {
        when(gameSellRepository.findGameSell_ByUserIdAndGameId(1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/remove-game-from-sell/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(gameSellRepository).findGameSell_ByUserIdAndGameId(1, -1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeGameFromSellShouldReturnNotFound_BothParameters() throws Exception {
        when(gameSellRepository.findGameSell_ByUserIdAndGameId(-1, -1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/-1/remove-game-from-sell/-1"))
                .andDo(print()).andExpect(status().isNotFound());

        verify(gameSellRepository).findGameSell_ByUserIdAndGameId(-1, -1);
    }

}
