package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumMessageDTO;
import com.petproject.boardgamefun.dto.request.ForumMessageRequest;
import com.petproject.boardgamefun.service.ForumMessageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation
            (value = "Get forum messages by User or Forum", notes = "Return all messages")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Messages successfully retrieved")
    })
    @GetMapping("/")
    public ResponseEntity<List<ForumMessageDTO>> getMessages(@RequestParam(required = false) Integer forumId, @RequestParam(required = false) Integer userId) {
        var forumMessagesDTO = forumMessageService.getMessages(forumId, userId);
        return new ResponseEntity<>(forumMessagesDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add a message to forum", notes = "Return all messages")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message successfully added"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Forum with that id does not exist or User with that id does not exist")
    })
    @PostMapping("/{forumId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ForumMessageDTO>> addMessage(@PathVariable Integer forumId, @PathVariable Integer userId, @RequestBody ForumMessageRequest forumMessageRequest) {
        var forumMessagesDTO = forumMessageService.addMessage(forumId, userId, forumMessageRequest);
        return new ResponseEntity<>(forumMessagesDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update a forum message ", notes = "Return all messages")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message successfully updated"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Message with that id not found"),
    })
    @PatchMapping("/{forumId}/{messageId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ForumMessageDTO>> updateMessage(@PathVariable Integer messageId, @PathVariable Integer forumId, @RequestBody ForumMessageRequest forumMessageRequest) {
        var messages = forumMessageService.updateMessage(messageId, forumId, forumMessageRequest);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete a forum message ", notes = "Return all messages")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Message with that id not found"),
    })
    @DeleteMapping("/{forumId}/{messageId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ForumMessageDTO>> deleteMessage(@PathVariable Integer forumId, @PathVariable Integer messageId) {
        var messages = forumMessageService.deleteMessage(forumId, messageId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
