package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumMessageDTO;
import com.petproject.boardgamefun.dto.request.ForumMessageRequest;
import com.petproject.boardgamefun.service.ForumMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("forum-messages")
public class ForumMessageController {
    private final ForumMessageService forumMessageService;

    public ForumMessageController(ForumMessageService forumMessageService) {
        this.forumMessageService = forumMessageService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ForumMessageDTO>> getMessages(@RequestParam(required = false) Integer forumId, @RequestParam(required = false) Integer userId) {
        var forumMessagesDTO = forumMessageService.getMessages(forumId, userId);
        return new ResponseEntity<>(forumMessagesDTO, HttpStatus.OK);
    }

    @PostMapping("/{forumId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ForumMessageDTO>> addMessage(@PathVariable Integer forumId, @PathVariable Integer userId, @RequestBody ForumMessageRequest forumMessageRequest) {
        var forumMessagesDTO = forumMessageService.addMessage(forumId, userId, forumMessageRequest);
        return new ResponseEntity<>(forumMessagesDTO, HttpStatus.OK);
    }

    @PatchMapping("/{forumId}/{messageId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ForumMessageDTO>> updateMessage(@PathVariable Integer messageId, @PathVariable Integer forumId, @RequestBody ForumMessageRequest forumMessageRequest) {
        var messages = forumMessageService.updateMessage(messageId, forumId, forumMessageRequest);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{forumId}/{messageId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ForumMessageDTO>> deleteMessage(@PathVariable Integer forumId, @PathVariable Integer messageId) {
        var messages = forumMessageService.deleteMessage(forumId, messageId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
