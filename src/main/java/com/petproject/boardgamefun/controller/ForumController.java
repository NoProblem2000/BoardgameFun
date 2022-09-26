package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumDataDTO;
import com.petproject.boardgamefun.dto.request.ForumRequest;
import com.petproject.boardgamefun.service.ForumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {
    private final ForumService forumService;
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("")
    public ResponseEntity<List<ForumDataDTO>> getForums(@RequestParam(required = false) Integer gameId, @RequestParam(required = false) Integer userId) {
        var forums = forumService.getForums(gameId, userId);
        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    @GetMapping("/{forumId}")
    public ResponseEntity<ForumDataDTO> getForum(@PathVariable Integer forumId) {
        var forum = forumService.getForum(forumId);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @PostMapping("add-forum/{gameId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> addForum(@PathVariable Integer gameId, @PathVariable Integer userId, @RequestBody ForumRequest forumRequest) {
        var forumDTO = forumService.addForum(gameId, userId, forumRequest);
        return new ResponseEntity<>(forumDTO, HttpStatus.OK);
    }

    @PatchMapping("/update-forum/{forumId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> updateForum(@PathVariable Integer forumId, @RequestBody ForumRequest forumRequest) {
        var forumDTO = forumService.updateForum(forumId, forumRequest);
        return new ResponseEntity<>(forumDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete-forum/{forumId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteForum(@PathVariable Integer forumId) {
        String title = forumService.deleteForum(forumId);
        return new ResponseEntity<>("Топик " + title + " удален из игры", HttpStatus.OK);
    }

    //todo: union patch method???
    //todo: forumDTO with rating and count messages
}
