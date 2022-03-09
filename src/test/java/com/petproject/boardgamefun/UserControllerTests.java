package com.petproject.boardgamefun;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    public UserControllerTests() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
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

        User admin = new User();
        admin.setName("Admin");
        admin.setRole("ROLE_ADMIN");
        admin.setMail("chupacabra@mail.ru");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/" + Gateway + "/{userId}", "1")).andExpect(status().isOk()).andReturn();

        UserDTO userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserDTO.class);

        Assertions.assertEquals(userResponse.getUser().getName(), admin.getName());
        Assertions.assertEquals(userResponse.getUser().getMail(), admin.getMail());
        Assertions.assertEquals(userResponse.getUser().getRole(), admin.getRole());
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
