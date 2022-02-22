package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    public List<UserDTO> entitiesToUserDTO(List<User> users){
        List<UserDTO> usersDTO = new ArrayList<>();
        for (var user :
                users) {
            usersDTO.add(new UserDTO(user));
        }
        return usersDTO;
    }

    public UserDTO entityToUserDTO(User user){
        return new UserDTO(user);
    }
}
