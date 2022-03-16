package com.petproject.boardgamefun;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;

import com.petproject.boardgamefun.security.model.JwtResponse;
import com.petproject.boardgamefun.security.model.LoginRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringSecurityWebTestConfig.class
)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTests {

    private final String Gateway = "users";

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    User admin;

    public UserControllerTests() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        admin = new User();
        admin.setName("Admin");
        admin.setRole("ROLE_ADMIN");
        admin.setMail("chupacabra@mail.ru");
    }

    @Test
    public void getUsersShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway)).andDo(print()).andExpect(status().isOk());
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
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getUserShouldReturnStatusNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void getUserWhenBadPathValueReturn400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/{userId}", "userId")).andExpect(status().isBadRequest());
    }

    @Test
    public void getUserWhenValidInput_thenReturnUserResource() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/{userId}", "1")).andExpect(status().isOk()).andReturn();

        UserDTO userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserDTO.class);

        Assertions.assertEquals(userResponse.getUser().getName(), admin.getName());
        Assertions.assertEquals(userResponse.getUser().getMail(), admin.getMail());
        Assertions.assertEquals(userResponse.getUser().getRole(), admin.getRole());
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
    @Order(2)
    public void deleteGameRatingShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game-rating/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(1)
    public void setGameRatingShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/set-game-rating/1/10")).andDo(print()).andExpect(status().isOk());
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
    @Order(1)
    public void addGameToUserShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/" + Gateway + "/1/add-game/1")).andDo(print()).andExpect(status().isOk());
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
    @Order(2)
    public void deleteGameFromUserCollectionShouldReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/" + Gateway + "/1/delete-game/1")).andDo(print()).andExpect(status().isOk());
    }

}
