package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;
import com.petproject.boardgamefun.service.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserDTO> entitiesToUserDTO(List<User> users) {
        List<UserDTO> usersDTO = new ArrayList<>();
        for (var user :
                users) {
            usersDTO.add(userMapper.userToUserDTO(user));
        }
        return usersDTO;
    }

    public UserDTO entityToUserDTO(User user) {
        return userMapper.userToUserDTO(user);
    }
}
