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
import com.petproject.boardgamefun.repository.ExpansionRepository;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;


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

    @Autowired
    private WebApplicationContext webApplicationContext;

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

    @MockBean
    private ExpansionRepository expansionRepository;

    private Game game;
    private Game game2;
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
    private List<FilterGamesDTO> filterGamesDTOList;
    private MockMultipartFile multipartFile;
    private Expansion expansion;

    @BeforeAll
    public void setup() {

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        multipartFile = new MockMultipartFile(
                "picture",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "0x36".getBytes()
        );

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

        games = new ArrayList<>();
        games.add(game);
        games.add(game2);

        expansion = new Expansion();
        expansion.setId(1);
        expansion.setParentGame(game);
        expansion.setDaughterGame(game2);

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
        when(gameRepository.findGameWithRating(1)).thenReturn(gameProjection);
        when(designerRepository.findDesignersUsingGame(1)).thenReturn(designersProjectionList);
        when(gameService.projectionsToGameDTO(gameProjection, designersProjectionList)).thenReturn(gameDataDTO);

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/get-game/1")).andDo(print()).andExpect(status().isOk()).andReturn();

        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO.class);

        verify(gameRepository).findGameWithRating(1);
        verify(designerRepository).findDesignersUsingGame(1);
        verify(gameService).projectionsToGameDTO(gameProjection, designersProjectionList);

        Assertions.assertEquals(res.getGame().id(), gameDataDTO.getGame().id());
    }

    @Test
    public void getGameByCriteriaShouldReturnNotFound() throws Exception {
        when(gameRepository.findGameWithRating(-1)).thenReturn(null);
        when(designerRepository.findDesignersUsingGame(-1)).thenReturn(null);
        when(gameService.projectionsToGameDTO(null, null)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/get-game/-1")).andDo(print()).andExpect(status().isNotFound());

        verify(gameRepository).findGameWithRating(-1);
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

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void addGameShouldReturnOk() throws Exception {
        game.setId(null);
        when(gameRepository.save(game)).thenReturn(game);
        when(gameService.entityToGameDTO(game)).thenReturn(gameDataDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isOk());

        verify(gameRepository).save(refEq(game));
        verify(gameService).entityToGameDTO(refEq(game));
        game.setId(1);
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void addGameShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void addGameShouldReturnBadRequest_BadModel() throws Exception {
        game.setTimeToPlayMax(null);
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isBadRequest());
        game.setTimeToPlayMax(360);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addGameShouldReturnForbidden() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isForbidden());
    }

    @Test
    public void addGameShouldReturnUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void uploadImageShouldReturnIsOk() throws Exception {
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(gameRepository.save(game)).thenReturn(null);
        when(gameRepository.findGameWithRating(1)).thenReturn(gameProjection);
        when(designerRepository.findDesignersUsingGame(1)).thenReturn(designersProjectionList);
        when(gameService.projectionsToGameDTO(gameProjection, designersProjectionList)).thenReturn(gameDataDTO);

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart(Gateway + "/upload-image/1");
        mockMvc.perform(multipartRequest.file(multipartFile))
                .andExpect(status().isOk());

        verify(gameRepository).findGameById(1);
        verify(gameRepository).save(game);
        verify(gameRepository).findGameWithRating(1);
        verify(designerRepository).findDesignersUsingGame(1);
        verify(gameService).projectionsToGameDTO(gameProjection, designersProjectionList);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void uploadImageShouldReturnNotFound() throws Exception {
        when(gameRepository.findGameById(-1)).thenReturn(null);

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart(Gateway + "/upload-image/-1");
        mockMvc.perform(multipartRequest.file(multipartFile))
                .andExpect(status().isNotFound());

        verify(gameRepository).findGameById(-1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void uploadImageShouldReturnBadRequest() throws Exception {
        multipartFile = new MockMultipartFile(
                "not-picture",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "0x36".getBytes()
        );
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart(Gateway + "/upload-image/-1");
        mockMvc.perform(multipartRequest.file(multipartFile))
                .andExpect(status().isBadRequest());

        multipartFile = new MockMultipartFile(
                "picture",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "0x36".getBytes()
        );
    }

    @Test
    public void uploadImageShouldReturnUnauthorized() throws Exception {
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart(Gateway + "/upload-image/-1");
        mockMvc.perform(multipartRequest.file(multipartFile))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void updateGameShouldReturnIsOk() throws Exception {
        when(gameRepository.save(game)).thenReturn(null);
        when(gameService.entityToGameDTO(game)).thenReturn(gameDataDTO);

        this.mockMvc.perform(MockMvcRequestBuilders.put(Gateway + "/update").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isOk());

        verify(gameRepository).save(refEq(game));
        verify(gameService).entityToGameDTO(refEq(game));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void updateGameShouldReturnIsBadRequest() throws Exception {
        game.setTimeToPlayMax(null);

        this.mockMvc.perform(MockMvcRequestBuilders.put(Gateway + "/update").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isBadRequest());

        game.setTimeToPlayMax(360);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateGameShouldReturnIsForbidden() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(Gateway + "/update").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isForbidden());

    }

    @Test
    public void updateGameShouldReturnIsUnauthorized() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(Gateway + "/update").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void removeGameFromSiteShouldReturnIsOk() throws Exception {
        doNothing().when(gameRepository).delete(game);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/remove").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isOk());

        verify(gameRepository).delete(refEq(game));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void removeGameFromSiteShouldReturnNotFound() throws Exception {
        game.setId(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/remove").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isNotFound());

        game.setId(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeGameFromSiteShouldReturnNotForbidden() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/remove").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isForbidden());
    }

    @Test
    public void removeGameFromSiteShouldReturnIsUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/remove").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game))).andExpect(status().isUnauthorized());
    }

    @Test
    public void getExpansionsShouldReturnIsOk() throws Exception {
        when(gameRepository.getExpansions(1)).thenReturn(games);
        when(gameService.entitiesToGameDTO(games)).thenReturn(gamesDataDTO);

        var mvsRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/expansions/1")).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvsRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).getExpansions(1);
        verify(gameService).entitiesToGameDTO(games);

        Assertions.assertEquals(gamesDataDTO.size(), res.length);
    }

    @Test
    public void getExpansionsShouldReturnIsOk_BlankArray() throws Exception {
        when(gameRepository.getExpansions(-1)).thenReturn(new ArrayList<>());
        when(gameService.entitiesToGameDTO(new ArrayList<>())).thenReturn(new ArrayList<>());

        var mvsRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/expansions/-1")).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvsRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).getExpansions(-1);
        verify(gameService).entitiesToGameDTO(new ArrayList<>());

        Assertions.assertEquals(0, res.length);
    }

    @Test
    public void getExpansionsShouldReturnIsNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/expansions/")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addExpansionShouldReturnIsOk() throws Exception {
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(gameRepository.findGameById(2)).thenReturn(game2);
        when(expansionRepository.save(any(Expansion.class))).thenReturn(expansion);
        when(gameRepository.getExpansions(1)).thenReturn(games);
        when(gameService.entitiesToGameDTO(games)).thenReturn(gamesDataDTO);

        var mvsRes = this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add-expansion/1/2")).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvsRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).findGameById(1);
        verify(gameRepository).findGameById(2);
        verify(expansionRepository).save(any(Expansion.class));
        verify(gameRepository).getExpansions(1);
        verify(gameService).entitiesToGameDTO(games);

        Assertions.assertEquals(gamesDataDTO.size(), res.length);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addExpansionShouldReturnIsBadRequest_ParentGame() throws Exception {
        when(gameRepository.findGameById(-1)).thenReturn(null);
        when(gameRepository.findGameById(2)).thenReturn(game2);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add-expansion/-1/2")).andExpect(status().isBadRequest());

        verify(gameRepository).findGameById(-1);
        verify(gameRepository).findGameById(2);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addExpansionShouldReturnIsBadRequest_DaughterGame() throws Exception {
        when(gameRepository.findGameById(1)).thenReturn(game);
        when(gameRepository.findGameById(-2)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add-expansion/1/-2")).andExpect(status().isBadRequest());

        verify(gameRepository).findGameById(1);
        verify(gameRepository).findGameById(-2);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addExpansionShouldReturnIsBadRequest_BothGame() throws Exception {
        when(gameRepository.findGameById(-1)).thenReturn(null);
        when(gameRepository.findGameById(-2)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add-expansion/-1/-2")).andExpect(status().isBadRequest());

        verify(gameRepository).findGameById(-1);
        verify(gameRepository).findGameById(-2);
    }

    @Test
    public void addExpansionShouldReturnIsUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(Gateway + "/add-expansion/1/2")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteExpansionShouldReturnIsOk() throws Exception {
        when(expansionRepository.findExpansion_ByParentGameIdAndDaughterGameId(1, 2)).thenReturn(expansion);
        doNothing().when(expansionRepository).delete(expansion);
        when(gameRepository.getExpansions(1)).thenReturn(games);
        when(gameService.entitiesToGameDTO(games)).thenReturn(gamesDataDTO);

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/delete-expansion/1/2")).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(expansionRepository).findExpansion_ByParentGameIdAndDaughterGameId(1, 2);
        verify(expansionRepository).delete(expansion);
        verify(gameRepository).getExpansions(1);
        verify(gameService).entitiesToGameDTO(games);

        Assertions.assertEquals(gamesDataDTO.size(), res.length);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteExpansionShouldReturnIsNotFound() throws Exception {
        when(expansionRepository.findExpansion_ByParentGameIdAndDaughterGameId(-1, -2)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/delete-expansion/-1/-2")).andExpect(status().isNotFound());

        verify(expansionRepository).findExpansion_ByParentGameIdAndDaughterGameId(-1, -2);
    }

    @Test
    public void deleteExpansionShouldReturnIsUnauthorized() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Gateway + "/delete-expansion/1/2")).andExpect(status().isUnauthorized());
    }

    @Test
    public void getSimilarGamesShouldReturnIsOk() throws Exception {
        when(gameRepository.getSimilarGames(1)).thenReturn(games);
        when(gameService.entitiesToGameDTO(games)).thenReturn(gamesDataDTO);

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/similar/1")).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).getSimilarGames(1);
        verify(gameService).entitiesToGameDTO(games);

        Assertions.assertEquals(games.size(), res.length);

    }

    @Test
    public void getSimilarGamesShouldReturnIsOk_BlankArray() throws Exception {
        when(gameRepository.getSimilarGames(-1)).thenReturn(new ArrayList<>());
        when(gameService.entitiesToGameDTO(new ArrayList<>())).thenReturn(new ArrayList<>());

        var mvcRes = this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/similar/-1")).andExpect(status().isOk()).andReturn();
        var res = objectMapper.readValue(mvcRes.getResponse().getContentAsByteArray(), GameDataDTO[].class);

        verify(gameRepository).getSimilarGames(-1);
        verify(gameService).entitiesToGameDTO(new ArrayList<>());

        Assertions.assertEquals(0, res.length);
    }

    @Test
    public void getSimilarGamesShouldReturnIsNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(Gateway + "/similar/")).andExpect(status().isNotFound()).andReturn();
    }

}
