package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumDataDTO;
import com.petproject.boardgamefun.dto.request.ForumRatingRequest;
import com.petproject.boardgamefun.service.ForumRatingService;
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

    @PostMapping("/{forumId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> setForumRating(@PathVariable Integer forumId, @PathVariable Integer userId, @RequestBody ForumRatingRequest forumRatingRequest) {
        var forum = forumRatingService.setForumRating(forumId, userId, forumRatingRequest);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @DeleteMapping("/{forumId}/{ratingId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> removeRatingFromForum(@PathVariable Integer forumId, @PathVariable Integer ratingId) {
        var forum = forumRatingService.removeRatingFromForum(forumId, ratingId);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }
}
