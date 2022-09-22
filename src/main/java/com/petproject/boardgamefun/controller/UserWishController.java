package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.service.UserWishService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-wishlist")
public class UserWishController {

    final UserWishService userWishService;

    public UserWishController(UserWishService userWishService) {
        this.userWishService = userWishService;
    }

    @ApiOperation
            (value = "Add game to user wishlist", notes = "Return status of successfully added game to user wishlist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully added to user wishlist"),
            @ApiResponse(code = 400, message = "User wish already exist"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "User with this id does not exist"),
    })
    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addGameToUserWishlist(@PathVariable Integer userId, @PathVariable Integer gameId) {
        String gameTitle = userWishService.addGameToUserWishlist(userId, gameId);
        return new ResponseEntity<>(gameTitle + " добавлена в ваши желания", HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete game from user wishlist", notes = "Return status of deleted game from wishlist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully deleted from wishlist"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "User wish don't exist")
    })
    @DeleteMapping("{userWishId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteGameFromUserWishlist(@PathVariable Integer userWishId) {
        userWishService.deleteGameFromUserWishlist(userWishId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get user wishlist", notes = "Return user games from wishlist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Games from user wishlist successfully retrieved")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<GameDataDTO>> getUserWishlist(@PathVariable Integer id) {
        var games = userWishService.getUserWishList(id);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
