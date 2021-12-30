package com.petproject.boardgamefun.controller;

import com.petproject.boardgamefun.dto.ForumDTO;
import com.petproject.boardgamefun.dto.request.ForumMessageRequest;
import com.petproject.boardgamefun.dto.request.ForumRatingRequest;
import com.petproject.boardgamefun.dto.request.ForumRequest;
import com.petproject.boardgamefun.model.Forum;
import com.petproject.boardgamefun.model.ForumMessage;
import com.petproject.boardgamefun.model.ForumRating;
import com.petproject.boardgamefun.repository.*;
import com.petproject.boardgamefun.service.ForumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/forum")
public class ForumController {

    private final ForumRepository forumRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ForumMessageRepository forumMessageRepository;
    private final ForumRatingRepository forumRatingRepository;
    private final ForumService forumService;

    public ForumController(ForumRepository forumRepository, GameRepository gameRepository, UserRepository userRepository, ForumMessageRepository forumMessageRepository, ForumRatingRepository forumRatingRepository, ForumService forumService) {
        this.forumRepository = forumRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.forumMessageRepository = forumMessageRepository;
        this.forumRatingRepository = forumRatingRepository;
        this.forumService = forumService;
    }

    @Transactional
    @GetMapping("")
    public ResponseEntity<List<ForumDTO>> getForums(@RequestParam(required = false) Integer gameId, @RequestParam(required = false) Integer userId) {
        List<ForumDTO> forums;
        if (gameId != null) {
            forums = forumService.projectionsToForumDTO(forumRepository.findForumsGameWithRating(gameId));
        } else if (userId != null) {
            forums = forumService.projectionsToForumDTO(forumRepository.findForumsUserWithRating(userId));
        } else {
            forums = forumService.projectionsToForumDTO(forumRepository.findForumsWithRating());
        }

        return new ResponseEntity<>(forums, HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/{forumId}")
    public ResponseEntity<ForumDTO> getForum(@PathVariable Integer forumId){
        var forum = forumService.projectionToForumDTO(forumRepository.findForumWithRatingUsingId(forumId));

        return new ResponseEntity<>(forum, HttpStatus.OK);
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
    public ResponseEntity<String> deleteForum(@PathVariable Integer forumId) {

        var forum = forumRepository.findForumById(forumId);
        forumRepository.delete(forum);

        return new ResponseEntity<>("Топик " + forum.getTitle() + " удален из игры", HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/messages")
    public ResponseEntity<List<ForumMessage>> getComments(@RequestParam(required = false) Integer forumId, @RequestParam(required = false) Integer userId) {
        List<ForumMessage> messages;
        if (forumId != null)
            messages = forumMessageRepository.findByForumId(forumId);
        else if (userId != null)
            messages = forumMessageRepository.findByUserId(userId);
        else
            messages = forumMessageRepository.findAll();

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/{forumId}/add-message/{userId}")
    public ResponseEntity<List<ForumMessage>> addMessage(@PathVariable Integer forumId, @PathVariable Integer userId, @RequestBody ForumMessageRequest forumMessageRequest) {
        var forum = forumRepository.findForumById(forumId);
        var user = userRepository.findUserById(userId);

        var forumMessage = new ForumMessage();
        forumMessage.setForum(forum);
        forumMessage.setUser(user);
        forumMessage.setTime(OffsetDateTime.now());
        forumMessage.setComment(forumMessageRequest.getComment());

        forumMessageRepository.save(forumMessage);

        var messages = forumMessageRepository.findByForumId(forumId);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/{forumId}/update-message/{messageId}")
    public ResponseEntity<List<ForumMessage>> updateMessage(@PathVariable Integer messageId, @PathVariable Integer forumId, @RequestBody ForumMessageRequest forumMessageRequest) {
        var message = forumMessageRepository.findForumMessageById(messageId);

        if (!Objects.equals(forumMessageRequest.getComment(), message.getComment()))
            message.setComment(forumMessageRequest.getComment());

        forumMessageRepository.save(message);

        var messages = forumMessageRepository.findByForumId(forumId);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{forumId}/delete-message/{messageId}")
    public ResponseEntity<List<ForumMessage>> deleteMessage(@PathVariable Integer forumId, @PathVariable Integer messageId ) {
        var message = forumMessageRepository.findForumMessageById(messageId);

        forumMessageRepository.delete(message);

        var messages = forumMessageRepository.findByForumId(forumId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/{forumId}/set-rating/{userId}")
    public ResponseEntity<Forum> setForumRating(@PathVariable Integer forumId, @PathVariable Integer userId, @RequestBody ForumRatingRequest forumRatingRequest){

        var forumRating =  forumRatingRepository.findForumRating_ByForumIdAndUserId(forumId, userId);
        if (forumRating == null){
            var forum = forumRepository.findForumById(forumId);
            var user = userRepository.findUserById(userId);

            forumRating = new ForumRating();
            forumRating.setForum(forum);
            forumRating.setUser(user);
        }
        forumRating.setRating(forumRatingRequest.getRating());

        forumRatingRepository.save(forumRating);
        var forum = forumRepository.findForumById(forumId);
        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{forumId}/remove-rating/{ratingId}")
    public ResponseEntity<Forum> removeRatingFromForum(@PathVariable Integer forumId, @PathVariable Integer ratingId){
        var forumRating = forumRatingRepository.findForumRatingById(ratingId);
        forumRatingRepository.delete(forumRating);

        var forum = forumRepository.findForumById(forumId);

        return new ResponseEntity<>(forum, HttpStatus.OK);
    }

    //todo: union patch method???
    //todo: forumDTO with rating and count messages
}
