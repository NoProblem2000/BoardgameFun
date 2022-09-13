package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.service.UserOwnGameService;
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

    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addGameToUser(@PathVariable Integer userId, @PathVariable Integer gameId) {
        String gameTitle = userOwnGameService.addGameToUser(userId, gameId);
        return new ResponseEntity<>(gameTitle + " добавлена в вашу коллекцию", HttpStatus.OK);
    }

    @DeleteMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Integer> deleteGameFromUserCollection(@PathVariable Integer userId, @PathVariable Integer gameId) {
        userOwnGameService.deleteGameFromUserCollection(userId, gameId);
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }
}
