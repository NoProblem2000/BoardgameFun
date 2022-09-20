package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.dto.request.PasswordChangeRequest;
import com.petproject.boardgamefun.dto.request.UserEditRequest;
import com.petproject.boardgamefun.model.*;
import com.petproject.boardgamefun.security.jwt.JwtUtils;
import com.petproject.boardgamefun.security.model.JwtResponse;
import com.petproject.boardgamefun.security.model.LoginRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenRequest;
import com.petproject.boardgamefun.security.model.RefreshTokenResponse;
import com.petproject.boardgamefun.security.services.RefreshTokenService;
import com.petproject.boardgamefun.security.services.UserDetailsImpl;
import com.petproject.boardgamefun.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    final UserService userService;
    final JwtUtils jwtUtils;
    final AuthenticationManager authenticationManager;
    final RefreshTokenService refreshTokenService;

    public UserController(UserService userService,JwtUtils jwtUtils,AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        var users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        if (!userService.existsByName(loginRequest.getName())) {
            return new ResponseEntity<>("Пользователя с таким никнеймом не существует", HttpStatus.NOT_FOUND);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getName());
        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), refreshToken), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshTokenService.verifyExpiration(refreshToken)) {
            var token = jwtUtils.generateJwtToken(refreshTokenRequest.getUserName());
            return new ResponseEntity<>(new RefreshTokenResponse(token, refreshToken), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/upload-avatar/{userName}")
    public ResponseEntity<String> uploadAvatar(@PathVariable String userName, @RequestParam("avatar") MultipartFile file) throws IOException {
        this.userService.uploadAvatar(userName, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> editUser(@PathVariable Integer userId, @RequestBody UserEditRequest userEditRequest) {
        UserDTO user = userService.editUser(userId, userEditRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/change-password/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Integer userId, @RequestBody PasswordChangeRequest passwordRequest) {
        UserDTO user = userService.changePassword(userId, passwordRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        var userDTO = userService.getUser(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    //todo: optimize response - not whole model, only needed fields
    //todo: add news
    //todo: unique repository???

}
