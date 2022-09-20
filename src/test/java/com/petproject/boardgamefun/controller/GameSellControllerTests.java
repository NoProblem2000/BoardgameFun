package com.petproject.boardgamefun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.SpringSecurityWebTestConfig;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.exception.BadRequestException;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.model.GameSell;
import com.petproject.boardgamefun.model.User;
import com.petproject.boardgamefun.service.GameSellService;
import com.petproject.boardgamefun.service.mappers.GameMapper;
import com.petproject.boardgamefun.service.mappers.GameSellMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Matchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringSecurityWebTestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameSellControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String Gateway = "/game-sell";

    ObjectMapper objectMapper;
    @Autowired
    GameSellMapper gameSellMapper;
    @MockBean
    private GameSellService gameSellService;

    private GameSell gameSell;
    private GameSell gameSell2;
    private List<GameSellDTO> gameSellDTOList;

    @BeforeAll
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String instantExpected = "2014-12-22T10:15:30Z";

        Game game = new Game();
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
        game.setYearOfRelease(OffsetDateTime.parse(instantExpected));

        Game game2 = new Game();
        game2.setId(1);
        game2.setTitle(" Игра номер 2");
        game2.setDescription("Отличная игра войнушка номер 2");
        game2.setAnnotation("Отличная игра номер 2");
        game2.setPicture(null);
        game2.setPlayerAge("16");
        game2.setPlayersMin(2);
        game2.setPlayersMax(4);
        game2.setTimeToPlayMin(30);
        game2.setTimeToPlayMax(120);
        game2.setYearOfRelease(OffsetDateTime.parse(instantExpected));

        User user = new User();
        user.setId(34);
        user.setName("Bobby");
        user.setPassword("1234qwer");
        user.setRole("USER");
        user.setMail("bobby@ggogle.com");
        user.setRating(1.0);
        user.setRegistrationDate(OffsetDateTime.now());

        gameSell = new GameSell();
        gameSell.setGame(game);
        gameSell.setCondition("good");
        gameSell.setComment("so good");
        gameSell.setPrice(100);
        gameSell.setUser(user);

        GameSell gameSell2 = new GameSell();
        gameSell2.setGame(game2);
        gameSell2.setCondition("bad");
        gameSell2.setComment("so bad");
        gameSell2.setPrice(1);
        gameSell2.setUser(user);

        gameSellDTOList = new ArrayList<>();
        gameSellDTOList.add(gameSellMapper.GameSellToGameSellDTO(gameSell));
        gameSellDTOList.add(gameSellMapper.GameSellToGameSellDTO(gameSell2));

    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnOk() throws Exception {
        when(gameSellService.addGameToSellList(eq(1), eq(1), any(GameSell.class))).thenReturn(gameSellDTOList);
        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSell))).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameSellDTO[].class);
        verify(gameSellService).addGameToSellList(eq(1), eq(1), any(GameSell.class));
        Assertions.assertEquals(res.length, 2);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnNotFound() throws Exception {
        when(gameSellService.addGameToSellList(eq(-1), eq(-1), any(GameSell.class))).thenThrow(NoRecordFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/-1/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSell))).andExpect(status().isNotFound());
        verify(gameSellService).addGameToSellList(eq(-1), eq(-1), any(GameSell.class));
        Assertions.assertThrows(NoRecordFoundException.class, () -> gameSellService.addGameToSellList(-1, -1, gameSell));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameToSellListShouldReturnBadRequest() throws Exception {
        when(gameSellService.addGameToSellList(eq(1), eq(1), any(GameSell.class))).thenThrow(BadRequestException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSell))).andExpect(status().isBadRequest());
        verify(gameSellService).addGameToSellList(eq(1), eq(1), any(GameSell.class));
        Assertions.assertThrows(BadRequestException.class, () -> gameSellService.addGameToSellList(1, 1, gameSell));
    }

    @Test
    public void addGameToSellListShouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSell))).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeGameFromSellListShouldReturnOk() throws Exception {
        doNothing().when(gameSellService).removeGameFromSell(1, 1);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/1")).andDo(print()).andExpect(status().isOk());
        verify(gameSellService).removeGameFromSell(1, 1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeGameFromSellListShouldReturnNotFound() throws Exception {
        doThrow(NoRecordFoundException.class).when(gameSellService).removeGameFromSell(1, 1);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/1")).andDo(print()).andExpect(status().isNotFound());
        verify(gameSellService).removeGameFromSell(1, 1);
        Assertions.assertThrows(NoRecordFoundException.class, () -> gameSellService.removeGameFromSell(1, 1));
    }

    @Test
    public void removeGameFromSellListShouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/1/1")).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateSellGameListShouldReturnOk() throws Exception {
        doNothing().when(gameSellService).updateGameSell(any(GameSell.class));
        this.mockMvc.perform(MockMvcRequestBuilders.put(Gateway)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSell))).andExpect(status().isOk());
        verify(gameSellService).updateGameSell( any(GameSell.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateSellGameListShouldReturnBadRequest() throws Exception {
        doThrow(BadRequestException.class).when(gameSellService).updateGameSell(any(GameSell.class));
        this.mockMvc.perform(MockMvcRequestBuilders.put(Gateway)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameSell))).andExpect(status().isBadRequest());
        verify(gameSellService).updateGameSell( any(GameSell.class));
        Assertions.assertThrows(BadRequestException.class, () -> gameSellService.updateGameSell(gameSell));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getGameSellListShouldReturnOk() throws Exception {
        when(gameSellService.getGameSellList(1)).thenReturn(gameSellDTOList);
        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameSellDTO[].class);
        verify(gameSellService).getGameSellList(1);
        Assertions.assertEquals(res.length, 2);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getGameSellListShouldReturnOk_BlankList() throws Exception {
        when(gameSellService.getGameSellList(1)).thenReturn(new ArrayList<>());
        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/1")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameSellDTO[].class);
        verify(gameSellService).getGameSellList(1);
        Assertions.assertEquals(res.length, 0);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getGameSellListShouldReturnNotFound() throws Exception {
        when(gameSellService.getGameSellList(-1)).thenThrow(NoRecordFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/-1")).andDo(print()).andExpect(status().isNotFound());
        verify(gameSellService).getGameSellList(-1);
        Assertions.assertThrows(NoRecordFoundException.class, () -> gameSellService.getGameSellList(-1));
    }
}
