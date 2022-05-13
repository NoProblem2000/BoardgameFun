package com.petproject.boardgamefun;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.DesignerDTO;
import com.petproject.boardgamefun.dto.FilterGamesDTO;
import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.dto.projection.DesignersProjection;
import com.petproject.boardgamefun.dto.projection.GameProjection;
import com.petproject.boardgamefun.dto.projection.GamesFilterByTitleProjection;
import com.petproject.boardgamefun.model.*;
import com.petproject.boardgamefun.repository.DesignerRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SpringSecurityWebTestConfig.class)
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

    @MockBean
    private DesignerRepository designerRepository;


    private Game game;
    private List<Game> games;
    private GameProjection gameProjection;
    private List<GameProjection> gameProjectionList;
    private GameDataDTO gameDataDTO;
    private List<GameDataDTO> gamesDataDTO;
    private Designer designer;
    private List<DesignersProjection> designersProjectionList;
    private DesignersProjection designersProjection;
    private DesignerDTO designerDTO;
    private List<GamesFilterByTitleProjection> gamesFilterByTitleProjectionList;
    List<FilterGamesDTO> filterGamesDTOList;


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

        gameProjection = new GamePOJO(game, 8.0);
        gameProjectionList = new ArrayList<>();
        gameProjectionList.add(gameProjection);

        designer = new Designer();
        designer.setId(1);
        designer.setName("Designer number one");

        Designer designer2 = new Designer();
        designer2.setId(2);
        designer2.setName("Designer number two");

        List<String> designers = new ArrayList<>();
        designers.add(designer.getName());
        designers.add(designer2.getName());

        designersProjectionList = new ArrayList<>();
        designersProjectionList.add(new DesignerPOJO(designer.getName()));
        designersProjectionList.add(new DesignerPOJO(designer2.getName()));

        gameDataDTO = new GameDataDTO(gameMapper.gameToGameDTO(game), 8.4, designers);
        designers.remove(1);
        GameDataDTO gameDataDTO1 = new GameDataDTO(gameMapper.gameToGameDTO(game), 7.9, designers);

        gamesDataDTO = new ArrayList<>();
        gamesDataDTO.add(gameDataDTO);
        gamesDataDTO.add(gameDataDTO1);

        gamesFilterByTitleProjectionList = new ArrayList<>();
        gamesFilterByTitleProjectionList.add(new GameTitlePOJO("some title", 2));
        gamesFilterByTitleProjectionList.add(new GameTitlePOJO("some title 2", 3));

        filterGamesDTOList = new ArrayList<>();
        filterGamesDTOList.add(new FilterGamesDTO(2, "some title"));
        filterGamesDTOList.add(new FilterGamesDTO(3, "some title 2"));

    }

    @Test
    public void getGamesShouldReturnOk() throws Exception {

        when(gameRepository.findGames()).thenReturn(gameProjectionList);
        when(gameService.projectionsToGameDTO(gameProjectionList)).thenReturn(gamesDataDTO);

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway)).andDo(print()).andExpect(status().isOk()).andReturn();

        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).findGames();
        verify(gameService).projectionsToGameDTO(gameProjectionList);

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

    @Test
    public void getGameByCriteriaShouldReturnOk() throws Exception {
        when(gameRepository.findGame(1)).thenReturn(gameProjection);
        when(designerRepository.findDesignersUsingGame(1)).thenReturn(designersProjectionList);
        when(gameService.projectionsToGameDTO(gameProjection, designersProjectionList)).thenReturn(gameDataDTO);

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/get-game/1")).andDo(print()).andExpect(status().isOk()).andReturn();

        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO.class);

        verify(gameRepository).findGame(1);
        verify(designerRepository).findDesignersUsingGame(1);
        verify(gameService).projectionsToGameDTO(gameProjection, designersProjectionList);

        Assertions.assertEquals(res.getGame().id(), gameDataDTO.getGame().id());
    }

    @Test
    public void getGameByCriteriaShouldReturnNotFound() throws Exception {
        when(gameRepository.findGame(-1)).thenReturn(null);
        when(designerRepository.findDesignersUsingGame(-1)).thenReturn(null);
        when(gameService.projectionsToGameDTO(null, null)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/get-game/-1")).andDo(print()).andExpect(status().isNotFound());

        verify(gameRepository).findGame(-1);
        verify(designerRepository).findDesignersUsingGame(-1);
        verify(gameService).projectionsToGameDTO(null, null);
    }

    @Test
    public void getGamesByTitleShouldReturnOk() throws Exception {
        when(gameRepository.findGamesUsingTitle("title")).thenReturn(gamesFilterByTitleProjectionList);
        when(gameService.getTitlesFromProjections(gamesFilterByTitleProjectionList)).thenReturn(filterGamesDTOList);

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/get-games-by-filter/title")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), FilterGamesDTO[].class);

        verify(gameRepository).findGamesUsingTitle("title");
        verify(gameService).getTitlesFromProjections(gamesFilterByTitleProjectionList);

        Assertions.assertEquals(gamesFilterByTitleProjectionList.size(), res.length);
    }

    @Test
    public void getGamesByTitleShouldReturnOk_BlankArray() throws Exception {
        when(gameRepository.findGamesUsingTitle("title not exist")).thenReturn(new ArrayList<>());
        when(gameService.getTitlesFromProjections(new ArrayList<>())).thenReturn(new ArrayList<>());

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/get-games-by-filter/title not exist")).andDo(print()).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), FilterGamesDTO[].class);

        verify(gameRepository).findGamesUsingTitle("title not exist");
        verify(gameService).getTitlesFromProjections(new ArrayList<>());

        Assertions.assertEquals(0, res.length);
    }

}
