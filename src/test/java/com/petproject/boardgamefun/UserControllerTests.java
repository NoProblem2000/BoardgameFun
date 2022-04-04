package com.petproject.boardgamefun;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;

import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.security.model.JwtResponse;
import com.petproject.boardgamefun.security.model.LoginRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenRequest;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringSecurityWebTestConfig.class
)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTests {

    private final String Gateway = "users";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private List<UserDTO> usersDTO;
    private UserDTO userDTO;
    private User userAdmin;
    private User userModerator;
    private User user;
    private List<User> users;

    @Autowired
    private MockMvc mockMvc;
    private static final int insertDataOrder = 1, checkOnDuplicate = 2, updateDataOrder = 3, deleteDataOrder = 4;

    @Captor
    ArgumentCaptor<List<User>> userListArgumentCaptor;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

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
        user.setPassword("pswdbobby");
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
    }

    @Test
    public void getUsersShouldReturnOk() throws Exception {
        when(userRepository.findAll()).thenReturn(users);
        when(userService.entitiesToUserDTO(users)).thenReturn(usersDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway)).andDo(print()).andExpect(status().isOk());


        verify(userRepository, only()).findAll();
        verify(userService, only()).entitiesToUserDTO(userListArgumentCaptor.capture());
        Assertions.assertEquals(userListArgumentCaptor.getValue().size(), 3);
    }

    @Test
    public void getUsersShouldReturnOk_BlankList() throws Exception {
        when(userRepository.findAll()).thenReturn(null);
        when(userService.entitiesToUserDTO(new ArrayList<>())).thenReturn(new ArrayList<>());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway)).andDo(print()).andExpect(status().isOk()).andReturn();
        UserDTO[] userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserDTO[].class);
        verify(userRepository, only()).findAll();
        verify(userService, only()).entitiesToUserDTO(userListArgumentCaptor.capture());
        Assertions.assertNull(userListArgumentCaptor.getValue());
        Assertions.assertEquals(userResponse.length, 0);
    }

    @Test
    public void authenticateUserShouldReturnOk() throws Exception {
        LoginRequest loginRequest = new LoginRequest("Admin", "123qweAdmin");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/sign-in").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk());
    }

    @Test
    public void authenticateUserShouldReturn415() throws Exception {
        LoginRequest loginRequest = new LoginRequest("Admin", "123qweAdmin");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/sign-in")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void authenticateUserShouldReturnNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest("-1Admin", "123qweAdmin");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isNotFound());
    }

    @Test
    public void authenticateUserShouldReturnNotAuthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("Admin", "qweAdmin");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized());
    }

    @Test
    public void authenticateUserShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void refreshTokenShouldReturnNotAuthorizedBadAccessToken() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("Admin", "bla-bla");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void refreshTokenShouldReturnIsOk() throws Exception {

        LoginRequest loginRequest = new LoginRequest("Admin", "123qweAdmin");
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/sign-in").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk()).andReturn();

        JwtResponse jwtResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), JwtResponse.class);

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(jwtResponse.getUserName(), jwtResponse.getRefreshToken());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserShouldReturnStatusOkTest() throws Exception {
        when(userRepository.findUserById(1)).thenReturn(user);
        when(userService.entityToUserDTO(user)).thenReturn(userDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/1")).andDo(print()).andExpect(status().isOk());
        verify(userRepository, only()).findUserById(1);
        verify(userService, only()).entityToUserDTO(userArgumentCaptor.capture());
        Assertions.assertEquals(userArgumentCaptor.getValue().getName(), userDTO.getUser().getName());
    }

    @Test
    public void getUserShouldReturnStatusNotFound() throws Exception {
        when(userRepository.findUserById(-1)).thenReturn(null);
        when(userService.entityToUserDTO(null)).thenReturn(new UserDTO());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/-1")).andDo(print()).andExpect(status().isNotFound());
        verify(userRepository, only()).findUserById(-1);
        verify(userService, only()).entityToUserDTO(null);
    }

    @Test
    public void getUserWhenBadPathValueReturn400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/{userId}", "userId")).andExpect(status().isBadRequest());
    }

    @Test
    public void getUserWhenValidInput_thenReturnUserResource() throws Exception {

        when(userRepository.findUserById(1)).thenReturn(user);
        when(userService.entityToUserDTO(user)).thenReturn(userDTO);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/{userId}", "1")).andExpect(status().isOk()).andReturn();

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
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/1/games")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserCollectionShouldReturnBlankArray() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/-1/games")).andExpect(status().isOk()).andReturn();
        var gameDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);
        Assertions.assertEquals(0, gameDTOS.length);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserRatingListShouldReturnIsOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/1/games-rating")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserRatingListShouldReturnBlankArray() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/-1/games-rating")).andExpect(status().isOk()).andReturn();
        var gameDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);
        Assertions.assertEquals(0, gameDTOS.length);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameRatingShouldReturnNotFound_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/-1/delete-game-rating/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameRatingShouldReturnNotFound_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game-rating/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameRatingShouldReturnNotFound_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/-1/delete-game-rating/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(deleteDataOrder)
    public void deleteGameRatingShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game-rating/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(insertDataOrder)
    public void setGameRatingShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/set-game-rating/1/10")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(checkOnDuplicate)
    public void setGameRatingShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/set-game-rating/1/10")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnBadRequestLessThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/set-game-rating/1/0")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnBadRequestMoreThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/set-game-rating/1/11")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnNotFound_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-1/set-game-rating/1/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnNotFound_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/set-game-rating/-1/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void setGameRatingShouldReturnNotFound_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-1/set-game-rating/-1/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(updateDataOrder)
    public void updateGameRatingShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/update-game-rating/1/10")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnBadRequestLessThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/update-game-rating/1/0")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnBadRequestMoreThanNormal() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/update-game-rating/1/11")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnNotFound_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-1/update-game-rating/1/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnNotFound_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/update-game-rating/-1/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameRatingShouldReturnNotFound_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-1/update-game-rating/-1/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(insertDataOrder)
    public void addGameToUserShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(checkOnDuplicate)
    public void addGameToUserShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusNotFound_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-11/add-game/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusNotFound_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusNotFound_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-1/add-game/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusBadRequest_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/bla/add-game/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusBadRequest_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserShouldReturnStatusBadRequest_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/bla/add-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnNotFound_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/-1/delete-game/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnNotFound_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnNotFound_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/-11/delete-game/-11")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnBadRequest_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/bla/delete-game/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnBadRequest_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteGameFromUserCollectionShouldReturnBadRequest_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/bla/delete-game/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(deleteDataOrder)
    public void deleteGameFromUserCollectionShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserWishlistShouldReturnIsOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/1/wishlist")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserWishlistShouldReturnBlankArray() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/-1/wishlist")).andExpect(status().isOk()).andReturn();
        var gameDTOS = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), GameDTO[].class);
        Assertions.assertEquals(0, gameDTOS.length);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserWishlistShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/bla/wishlist")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(insertDataOrder)
    public void addGameToUserWishlistShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game-to-wishlist/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(checkOnDuplicate)
    public void addGameToUserWishlistShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game-to-wishlist/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusNotFound_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-11/add-game-to-wishlist/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusNotFound_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game-to-wishlist/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusNotFound_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/-1/add-game-to-wishlist/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusBadRequest_FirstParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/bla/add-game-to-wishlist/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusBadRequest_SecondParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game-to-wishlist/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToUserWishlistShouldReturnStatusBadRequest_BothParameters() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/bla/add-game-to-wishlist/bla")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(deleteDataOrder)
    public void deleteGameFromUserWishlistShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game-from-wishlist/1")).andDo(print()).andExpect(status().isOk());
    }
}
