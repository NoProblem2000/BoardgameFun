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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    public UserController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @ApiOperation
            (value = "Get all users in site", notes = "Returns all users")
    @ApiResponse(code = 200, message = "Successfully retrieved")
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        var users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Authenticate the user", notes = "Returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticated"),
            @ApiResponse(code = 404, message = "User does not exist"),
            @ApiResponse(code = 401, message = "User nickname or password incorrect")})
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

    @ApiOperation
            (value = "User registration", notes = "Return sign-up user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully sign-up"),
            @ApiResponse(code = 400, message = "User with this nickname already exist"),
            @ApiResponse(code = 400, message = "User with this email already exist"),
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Refresh token", notes = "Return refresh token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Token successfully refreshed"),
            @ApiResponse(code = 401, message = "Refresh token is expired"),
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshTokenService.verifyExpiration(refreshToken)) {
            var token = jwtUtils.generateJwtToken(refreshTokenRequest.getUserName());
            return new ResponseEntity<>(new RefreshTokenResponse(token, refreshToken), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @ApiOperation
            (value = "Upload the avatar", notes = "Upload user avatar")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Image successfully uploaded"),
            @ApiResponse(code = 404, message = "User with this id not found"),
    })
    @PostMapping("/upload-avatar/{userName}")
    public ResponseEntity<String> uploadAvatar(@PathVariable String userName, @RequestParam("avatar") MultipartFile file) throws IOException {
        this.userService.uploadAvatar(userName, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation
            (value = "Edit user data", notes = "Return user with edited data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data was successfully updated"),
            @ApiResponse(code = 400, message = "User with this nickname already exists"),
    })
    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> editUser(@PathVariable Integer userId, @RequestBody UserEditRequest userEditRequest) {
        UserDTO user = userService.editUser(userId, userEditRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Edit user password", notes = "Return user with edited password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password was successfully updated"),
            @ApiResponse(code = 400, message = "You entered the same password"),
    })
    @PatchMapping("/change-password/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Integer userId, @RequestBody PasswordChangeRequest passwordRequest) {
        UserDTO user = userService.changePassword(userId, passwordRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get user by id", notes = "Return a user as per the id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully retrieved"),
            @ApiResponse(code = 400, message = "User with id not found"),
    })
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        var userDTO = userService.getUser(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    //todo: optimize response - not whole model, only needed fields
    //todo: unique repository???

}
