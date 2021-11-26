package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.model.User;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.security.jwt.JwtUtils;
import com.petproject.boardgamefun.security.model.JwtResponse;
import com.petproject.boardgamefun.security.model.LoginRequest;
import com.petproject.boardgamefun.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(){
        var users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        if (!userRepository.existsUserByName(loginRequest.getName())){
            return new ResponseEntity<>("Пользователя с таким никнеймом не существует", HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody User user){

        if (userRepository.existsUserByName(user.getName())){
            return new ResponseEntity<>("Пользователь с таким никнеймом уже существует", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsUserByMail(user.getMail())){
            return new ResponseEntity<>("Пользователь с такой почтой уже существует", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
