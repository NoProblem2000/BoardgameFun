package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.dto.request.PasswordChangeRequest;
import com.petproject.boardgamefun.dto.request.UserEditRequest;
import com.petproject.boardgamefun.exception.BadRequestException;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.User;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.UserOwnGameRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.security.enums.Role;
import com.petproject.boardgamefun.service.mappers.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    final UserMapper userMapper;
    final UserRepository userRepository;
    final GameRepository gameRepository;
    final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, UserRepository userRepository, GameRepository gameRepository, UserOwnGameRepository userOwnGameRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getUsers() {
        return entitiesToUserDTO(userRepository.findAll());
    }

    public Boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public void registerUser(User user) {
        if (userRepository.existsByName(user.getName())) {
            throw new BadRequestException("Пользователь с таким никнеймом уже существует");
        }

        if (userRepository.existsByMail(user.getMail())) {
            throw new BadRequestException("Пользователь с такой почтой уже существует");
        }

        user.setRole(Role.ROLE_USER.name());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(OffsetDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void uploadAvatar(String userName, MultipartFile multipartFile) throws IOException {
        var user = userRepository.findUserByName(userName);
        if (user == null) {
            throw new NoRecordFoundException("No user with name " + userName + " found");
        }
        user.setAvatar(multipartFile.getBytes());
        userRepository.save(user);
    }

    @Transactional
    public UserDTO editUser(Integer userId, UserEditRequest userEditRequest) {
        var user = userRepository.findUserById(userId);
        if (userEditRequest.getName() != null && !userRepository.existsByName(userEditRequest.getName())) {
            user.setName(userEditRequest.getName());
        } else {
            throw new BadRequestException("Пользователь с таким никнеймом уже существует");
        }
        if (userEditRequest.getRole() != null && !Objects.equals(userEditRequest.getRole(), user.getRole())) {
            user.setRole(userEditRequest.getRole());
        }
        userRepository.save(user);

        return entityToUserDTO(user);
    }

    @Transactional
    public UserDTO changePassword(Integer userId, PasswordChangeRequest passwordRequest) {
        var user = userRepository.findUserById(userId);
        if (passwordRequest.getPassword() != null && passwordEncoder.matches(passwordRequest.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        } else {
            throw new BadRequestException("Вы ввели точно такой же пароль");
        }
        userRepository.save(user);
        return entityToUserDTO(user);

    }

    @Transactional
    public UserDTO getUser(Integer userId) {
        UserDTO userDTO = entityToUserDTO(userRepository.findUserById(userId));
        if (userDTO == null) {
            throw new NoRecordFoundException();
        }
        return userDTO;
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
