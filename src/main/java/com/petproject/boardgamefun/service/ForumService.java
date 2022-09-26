package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.ForumDataDTO;
import com.petproject.boardgamefun.dto.projection.ForumProjection;
import com.petproject.boardgamefun.dto.request.ForumRequest;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.Forum;
import com.petproject.boardgamefun.repository.ForumRepository;
import com.petproject.boardgamefun.repository.GameRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.service.mappers.ForumMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ForumService {

    final ForumMapper forumMapper;
    final ForumRepository forumRepository;
    final UserRepository userRepository;
    final GameRepository gameRepository;

    public ForumService(ForumMapper forumMapper, ForumRepository forumRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.forumMapper = forumMapper;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public List<ForumDataDTO> getForums(Integer gameId, Integer userId){
        List<ForumDataDTO> forums;
        if (gameId != null) {
            forums = projectionsToForumDTO(forumRepository.findForumsGameWithRating(gameId));
        } else if (userId != null) {
            forums = projectionsToForumDTO(forumRepository.findForumsUserWithRating(userId));
        } else {
            forums = projectionsToForumDTO(forumRepository.findForumsWithRating());
        }

        return forums;
    }

    @Transactional
    public ForumDataDTO getForum(Integer forumId){
        return projectionToForumDTO(forumRepository.findForumWithRatingUsingId(forumId));
    }

    @Transactional
    public ForumDataDTO addForum(Integer gameId, Integer userId, ForumRequest forumRequest){
        var user = userRepository.findUserById(userId);
        var game = gameRepository.findGameById(gameId);

        if (user == null || game == null){
            throw new NoRecordFoundException("Game or User not found");
        }
        var forum = new Forum();
        forum.setGame(game);
        forum.setUser(user);
        forum.setText(forumRequest.getText());
        forum.setTitle(forumRequest.getTitle());
        forum.setPublicationTime(OffsetDateTime.now());

        forumRepository.save(forum);

        return entityToForumDTO(forum);
    }

    @Transactional
    public ForumDataDTO updateForum(Integer forumId, ForumRequest forumRequest){
        var forum = forumRepository.findForumById(forumId);

        if(forum == null){
            throw new NoRecordFoundException("Forum not found");
        }
        if (forumRequest.getTitle() != null && !Objects.equals(forumRequest.getTitle(), forum.getTitle()))
            forum.setTitle(forumRequest.getTitle());

        if (forumRequest.getText() != null && !Objects.equals(forumRequest.getText(), forum.getText()))
            forum.setText(forumRequest.getText());

        forumRepository.save(forum);

        return entityToForumDTO(forum);
    }

    @Transactional
    public String deleteForum(Integer forumId){
        var forum = forumRepository.findForumById(forumId);
        if (forum == null)
            throw new NoRecordFoundException("Forum not found");
        forumRepository.delete(forum);
        return forum.getTitle();
    }

    public List<ForumDataDTO> projectionsToForumDTO(List<ForumProjection> projections) {
        ArrayList<ForumDataDTO> forums = new ArrayList<>();
        for (var projection : projections) {
            forums.add(new ForumDataDTO(forumMapper.forumToForumDTO(projection.getForum()), projection.getRating()));
        }
        return forums;
    }

    public ForumDataDTO projectionToForumDTO(ForumProjection projection) {
        ForumDataDTO forum = new ForumDataDTO();
        forum.setForum(forumMapper.forumToForumDTO(projection.getForum()));
        forum.setRating(projection.getRating());
        return forum;
    }

    public List<ForumDataDTO> entitiesToForumDTO(List<Forum> forums) {
        ArrayList<ForumDataDTO> forumsDTO = new ArrayList<>();
        for (var forum :
                forums) {
            forumsDTO.add(new ForumDataDTO(forumMapper.forumToForumDTO(forum), null));
        }
        return forumsDTO;
    }

    public ForumDataDTO entityToForumDTO(Forum forum) {
        return new ForumDataDTO(forumMapper.forumToForumDTO(forum), null);
    }
}
