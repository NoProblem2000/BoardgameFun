package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.service.UserOwnGameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-own-game")
public class UserOwnGameController {

    final UserOwnGameService userOwnGameService;

    public UserOwnGameController(UserOwnGameService userOwnGameService) {
        this.userOwnGameService = userOwnGameService;
    }

    @ApiOperation
            (value = "Add game to user collection", notes = "Return status of successfully added game to user collection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully added to user collection"),
            @ApiResponse(code = 400, message = "Game already exist in user collection"),
            @ApiResponse(code = 401, message = "You are not authorized to add game to collection"),
            @ApiResponse(code = 404, message = "User with this id does not exist"),
            @ApiResponse(code = 404, message = "Game with this id does not exist")
    })
    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addGameToUser(@PathVariable Integer userId, @PathVariable Integer gameId) {
        String gameTitle = userOwnGameService.addGameToUser(userId, gameId);
        return new ResponseEntity<>(gameTitle + " добавлена в вашу коллекцию", HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete game from user collection", notes = "Return id of deleted game from user collection")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game successfully added to user collection"),
            @ApiResponse(code = 401, message = "You are not authorized to delete game from collection"),
            @ApiResponse(code = 404, message = "Game with this id does not exist in user collection")
    })
    @DeleteMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteGameFromUserCollection(@PathVariable Integer userId, @PathVariable Integer gameId) {
        userOwnGameService.deleteGameFromUserCollection(userId, gameId);
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }
}
