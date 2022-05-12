package com.petproject.boardgamefun;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.projection.GameProjection;
import com.petproject.boardgamefun.model.Designer;
import com.petproject.boardgamefun.model.Game;
import com.petproject.boardgamefun.model.GamePOJO;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.service.GameService;
import com.petproject.boardgamefun.service.mappers.GameMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;

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
public class GameControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final String Gateway = "/games";

    ObjectMapper objectMapper;

    @Autowired
    GameMapper gameMapper;

    @MockBean
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;


    private Game game;
    private List<Game> games;
    private GameProjection gamePOJO;
    private List<GameProjection> gamePOJOList;
    private GameDataDTO gameDataDTO;
    private List<GameDataDTO> gamesDataDTO;
    private Designer designer;
    private DesignerDTO designerDTO;


    @BeforeAll
    public void setup() {

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

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

        gamePOJO = new GamePOJO(game, 8.0);
        gamePOJOList = new ArrayList<>();
        gamePOJOList.add(gamePOJO);

        designer = new Designer();
        designer.setId(1);
        designer.setName("Designer number one");

        Designer designer2 = new Designer();
        designer2.setId(2);
        designer2.setName("Designer number two");

        List<String> designers = new ArrayList<>();
        designers.add(designer.getName());
        designers.add(designer2.getName());

        gameDataDTO = new GameDataDTO(gameMapper.gameToGameDTO(game), 8.4, designers);
        designers.remove(1);
        GameDataDTO gameDataDTO1 = new GameDataDTO(gameMapper.gameToGameDTO(game), 7.9, designers);

        gamesDataDTO = new ArrayList<>();
        gamesDataDTO.add(gameDataDTO);
        gamesDataDTO.add(gameDataDTO1);


    }

    @Test
    public void getGamesShouldReturnOk() throws Exception {

        when(gameRepository.findGames()).thenReturn(gamePOJOList);
        when(gameService.projectionsToGameDTO(gamePOJOList)).thenReturn(gamesDataDTO);

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway)).andDo(print()).andExpect(status().isOk()).andReturn();

        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).findGames();
        verify(gameService).projectionsToGameDTO(gamePOJOList);

        Assertions.assertEquals(res.length, 2);

    }

    @Test
    public void getGamesShouldReturnOk_BlankList() throws Exception {

        when(gameRepository.findGames()).thenReturn(new ArrayList<>());
        when(gameService.projectionsToGameDTO(new ArrayList<>())).thenReturn(new ArrayList<>());

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway)).andDo(print()).andExpect(status().isOk()).andReturn();

        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).findGames();
        verify(gameService).projectionsToGameDTO(new ArrayList<>());

        Assertions.assertEquals(res.length, 0);

    }

}
