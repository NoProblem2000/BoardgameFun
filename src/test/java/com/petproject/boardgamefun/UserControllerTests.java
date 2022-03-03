package com.petproject.boardgamefun;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringSecurityWebTestConfig.class
)
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    public UserControllerTests(){
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void getUserShouldReturnStatusOkTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getUserShouldReturnStatusNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/-1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void getUserWhenBadPathValueReturn400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", "userId")).andExpect(status().isBadRequest());
    }

    @Test
    public void getUserWhenValidInput_thenReturnUserResource() throws Exception {

        User admin = new User();
        admin.setName("Admin");
        admin.setRole("ROLE_ADMIN");
        admin.setMail("chupacabra@mail.ru");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", "1")).andExpect(status().isOk()).andReturn();

        UserDTO userResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserDTO.class);

        Assertions.assertEquals(userResponse.getUser().getName(), admin.getName());
        Assertions.assertEquals(userResponse.getUser().getMail(), admin.getMail());
        Assertions.assertEquals(userResponse.getUser().getRole(), admin.getRole());
    }
}
