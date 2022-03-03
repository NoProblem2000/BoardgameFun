package com.petproject.boardgamefun;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebTestConfig.class
)
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getUserShouldReturnStatusOkTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getUserShouldReturnStatusNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void whenBadPathValueReturn400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", "userId")).andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidInput_thenReturnUserResource() throws Exception {

        User admin = new User();
        admin.setName("Admin");
        admin.setRole("ROLE_ADMIN");
        admin.setMail("chupacabra@mail.ru");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", "1")).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        UserDTO userResponse = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserDTO.class);

        assertEquals(userResponse.getUser().getName(), admin.getName());
        assertEquals(userResponse.getUser().getMail(), admin.getMail());
        assertEquals(userResponse.getUser().getRole(), admin.getRole());
    }
}
