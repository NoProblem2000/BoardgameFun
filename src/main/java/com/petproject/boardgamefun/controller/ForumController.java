package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumRequest;
import com.petproject.boardgamefun.model.Forum;
import com.petproject.boardgamefun.repository.ForumRepository;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/forum")
public class ForumController {

    private final ForumRepository forumRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public ForumController(ForumRepository forumRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.forumRepository = forumRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @GetMapping("")
    public ResponseEntity<List<Forum>> getForums(@RequestParam(required = false) Integer gameId, @RequestParam(required = false) Integer userId) {
       List<Forum> forums;
       if (gameId != null){
           forums = forumRepository.findForumsByGameId(gameId);
       }
       else if (userId != null){
           forums = forumRepository.findForumsByUserId(userId);
       }
       else {
           forums = forumRepository.findAll();
       }

        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("add-forum/{gameId}/{userId}")
    public ResponseEntity<Forum> addForum(@PathVariable Integer gameId, @PathVariable Integer userId, @RequestBody ForumRequest forumRequest) {

        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        var forum = new Forum();
        forum.setGame(game);
        forum.setUser(user);
        forum.setText(forumRequest.getText());
        forum.setTitle(forumRequest.getTitle());
        forum.setPublicationTime(OffsetDateTime.now());

        forumRepository.save(forum);

        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/update-forum/{forumId}")
    public ResponseEntity<Forum> updateForum(@PathVariable Integer forumId, @RequestBody ForumRequest forumRequest) {
        var forum = forumRepository.findForumById(forumId);

        if (forumRequest.getTitle() != null && !Objects.equals(forumRequest.getTitle(), forum.getTitle()))
            forum.setTitle(forumRequest.getTitle());

        if (forumRequest.getText() != null && !Objects.equals(forumRequest.getText(), forum.getText()))
            forum.setText(forumRequest.getText());

        forumRepository.save(forum);

        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete-forum/{forumId}")
    public ResponseEntity<String> deleteForum(@PathVariable Integer forumId){

        var forum = forumRepository.findForumById(forumId);
        forumRepository.delete(forum);

        return new ResponseEntity<>("Топик " + forum.getTitle() + " удален из игры", HttpStatus.OK);
    }

    //todo: union patch method???
    //todo: forumDTO with rating and count messages
}
