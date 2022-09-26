package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumDataDTO;
import com.petproject.boardgamefun.dto.request.ForumRequest;
import com.petproject.boardgamefun.service.ForumService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation
            (value = "Get forums", notes = "Return all forums")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Forums successfully retrieved")
    })
    @GetMapping("")
    public ResponseEntity<List<ForumDataDTO>> getForums(@RequestParam(required = false) Integer gameId, @RequestParam(required = false) Integer userId) {
        var forums = forumService.getForums(gameId, userId);
        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Get forum by id", notes = "Return forum model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Forum successfully retrieved")
    })
    @GetMapping("/{forumId}")
    public ResponseEntity<ForumDataDTO> getForum(@PathVariable Integer forumId) {
        var forum = forumService.getForum(forumId);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Add the forum", notes = "Return new forum model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Forum successfully added"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Game or User not found")
    })
    @PostMapping("add-forum/{gameId}/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> addForum(@PathVariable Integer gameId, @PathVariable Integer userId, @RequestBody ForumRequest forumRequest) {
        var forumDTO = forumService.addForum(gameId, userId, forumRequest);
        return new ResponseEntity<>(forumDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Update the forum data", notes = "Return updated forum model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Forum successfully updated"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Forum not found")
    })
    @PatchMapping("/update-forum/{forumId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ForumDataDTO> updateForum(@PathVariable Integer forumId, @RequestBody ForumRequest forumRequest) {
        var forumDTO = forumService.updateForum(forumId, forumRequest);
        return new ResponseEntity<>(forumDTO, HttpStatus.OK);
    }

    @ApiOperation
            (value = "Delete the forum", notes = "Return status of deleted forum")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Forum successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 404, message = "Forum not found")
    })
    @DeleteMapping("/delete-forum/{forumId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteForum(@PathVariable Integer forumId) {
        String title = forumService.deleteForum(forumId);
        return new ResponseEntity<>("Топик " + title + " удален из игры", HttpStatus.OK);
    }

    //todo: union patch method???
    //todo: forumDTO with rating and count messages
}
