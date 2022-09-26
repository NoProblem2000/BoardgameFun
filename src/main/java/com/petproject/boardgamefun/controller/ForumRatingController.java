package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumDataDTO;
import com.petproject.boardgamefun.dto.request.ForumRatingRequest;
import com.petproject.boardgamefun.service.ForumRatingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/forum-rating")
public class ForumRatingController {
    final ForumRatingService forumRatingService;

    public ForumRatingController(ForumRatingService forumRatingService) {
        this.forumRatingService = forumRatingService;
    }

    @ApiOperation
            (value = "Set rating for forum by user", notes = "Return forum model with rating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rating successfully set"),
            @ApiResponse(code = 404, message = "Forum with that id does not exist or User with that id does not exist"),
            @ApiResponse(code = 401, message = "You are not authorized")
    })
    @PostMapping("/{forumId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> setForumRating(@PathVariable Integer forumId, @PathVariable Integer userId, @RequestBody ForumRatingRequest forumRatingRequest) {
        var forum = forumRatingService.setForumRating(forumId, userId, forumRatingRequest);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Remove rating for forum by user", notes = "Return forum model with rating")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rating successfully removed"),
            @ApiResponse(code = 404, message = "Forum with that id does not exist or User with that id does not exist"),
            @ApiResponse(code = 401, message = "You are not authorized")
    })
    @DeleteMapping("/{forumId}/{ratingId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> removeRatingFromForum(@PathVariable Integer forumId, @PathVariable Integer ratingId) {
        var forum = forumRatingService.removeRatingFromForum(forumId, ratingId);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }
}
