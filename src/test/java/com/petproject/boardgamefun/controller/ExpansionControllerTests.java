package com.petproject.boardgamefun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.SpringSecurityWebTestConfig;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.Designer;
import com.petproject.boardgamefun.model.Expansion;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.service.ExpansionService;
import com.petproject.boardgamefun.service.GameService;
import com.petproject.boardgamefun.service.mappers.GameMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringSecurityWebTestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExpansionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String Gateway = "/expansions";

    ObjectMapper objectMapper;

    @Autowired
    GameMapper gameMapper;

    @MockBean
    private ExpansionService expansionService;

    private List<GameDataDTO> gamesDataDTO;
    private Game game;
    private Game game2;
    private GameDataDTO gameDataDTO;

    @BeforeAll
    public void setup(){
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String instantExpected = "2014-12-22T10:15:30Z";

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
        game.setYearOfRelease(OffsetDateTime.parse(instantExpected));

        game2 = new Game();
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

        Designer designer = new Designer();
        designer.setId(1);
        designer.setName("Designer number one");

        Designer designer2 = new Designer();
        designer2.setId(2);
        designer2.setName("Designer number two");

        List<String> designers = new ArrayList<>();
        designers.add(designer.getName());
        designers.add(designer2.getName());

        gameDataDTO = new GameDataDTO(gameMapper.gameToGameDTO(game), 8.4, designers);
        GameDataDTO gameDataDTO1 = new GameDataDTO(gameMapper.gameToGameDTO(game2), 7.9, designers);
        gamesDataDTO = new ArrayList<>();
        gamesDataDTO.add(gameDataDTO);
        gamesDataDTO.add(gameDataDTO1);
    }

    @Test
    public void getExpansionsShouldReturnOk() throws Exception {
        when(expansionService.getExpansions(1)).thenReturn(gamesDataDTO);
        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway+ "/1")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);
        verify(expansionService).getExpansions(1);
        Assertions.assertEquals(res.length, 2);
    }

    @Test
    public void getExpansionsShouldReturnOk_BlankArray() throws Exception {
        when(expansionService.getExpansions(1)).thenReturn(new ArrayList<>());
        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway+ "/1")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);
        verify(expansionService).getExpansions(1);
        Assertions.assertEquals(res.length, 0);
    }

    @Test
    public void getExpansionsShouldReturnNotFound() throws Exception {
        when(expansionService.getExpansions(-1)).thenThrow(new NoRecordFoundException());
        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway+ "/-1")).andDo(print()).andExpect(status().isNotFound());
        verify(expansionService).getExpansions(-1);
        Assertions.assertThrows(NoRecordFoundException.class, () ->expansionService.getExpansions(-1));
    }
    @Test
    @WithMockUser(roles = "USER")
    public void addExpansionsShouldReturnOK() throws Exception {
        when(expansionService.addExpansion(1, 2)).thenReturn(gamesDataDTO);
        var mvcRes =  this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway+ "/1/2")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);
        verify(expansionService).addExpansion(1,2);
        Assertions.assertEquals(res.length, 2);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addExpansionsShouldReturnNotFound() throws Exception {
        when(expansionService.addExpansion(-1, -2)).thenThrow(NoRecordFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway+ "/-1/-2")).andDo(print()).andExpect(status().isNotFound());
        verify(expansionService).addExpansion(-1,-2);
        Assertions.assertThrows(NoRecordFoundException.class, () -> expansionService.addExpansion(-1,-2));
    }

    @Test
    public void addExpansionsShouldReturnNotAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway+ "/1/2")).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteExpansionsShouldReturnOK() throws Exception {
        when(expansionService.deleteExpansion(1, 2)).thenReturn(gamesDataDTO);
        var mvcRes =  this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway+ "/1/2")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);
        verify(expansionService).deleteExpansion(1,2);
        Assertions.assertEquals(res.length, 2);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteExpansionsShouldReturnNotFound() throws Exception {
        when(expansionService.deleteExpansion(-1, -2)).thenThrow(NoRecordFoundException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway+ "/-1/-2")).andDo(print()).andExpect(status().isNotFound());
        verify(expansionService).deleteExpansion(-1,-2);
        Assertions.assertThrows(NoRecordFoundException.class, () -> expansionService.deleteExpansion(-1,-2));
    }

    @Test
    public void deleteExpansionsShouldReturnNotAuthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway+ "/1/2")).andDo(print()).andExpect(status().isUnauthorized());
    }


}
