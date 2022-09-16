package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.GameDataDTO;
import com.petproject.boardgamefun.service.UserWishService;
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

    @PostMapping("{userId}/{gameId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addGameToUserWishlist(@PathVariable Integer userId, @PathVariable Integer gameId) {
        String gameTitle = userWishService.addGameToUserWishlist(userId, gameId);
        return new ResponseEntity<>(gameTitle + " добавлена в ваши желания", HttpStatus.OK);
    }

    @DeleteMapping("{userWishId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteGameFromUserWishlist(@PathVariable Integer userWishId) {
        userWishService.deleteGameFromUserWishlist(userWishId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<GameDataDTO>> getUserWishlist(@PathVariable Integer id) {
        var games = userWishService.getUserWishList(id);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
