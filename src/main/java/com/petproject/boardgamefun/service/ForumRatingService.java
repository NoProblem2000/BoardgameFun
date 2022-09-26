package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.ForumDataDTO;
import com.petproject.boardgamefun.dto.request.ForumRatingRequest;
import com.petproject.boardgamefun.model.ForumRating;
import com.petproject.boardgamefun.repository.ForumRatingRepository;
import com.petproject.boardgamefun.repository.ForumRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ForumRatingService {

    final ForumRatingRepository forumRatingRepository;
    final ForumRepository forumRepository;
    final UserRepository userRepository;
    final ForumService forumService;

    public ForumRatingService(ForumRatingRepository forumRatingRepository, ForumRepository forumRepository, UserRepository userRepository, ForumService forumService) {
        this.forumRatingRepository = forumRatingRepository;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
        this.forumService = forumService;
    }

    @Transactional
    public ForumDataDTO setForumRating(Integer forumId, Integer userId, ForumRatingRequest forumRequest) {
        var forumRating = forumRatingRepository.findForumRating_ByForumIdAndUserId(forumId, userId);
        if (forumRating == null) {
            var forum = forumRepository.findForumById(forumId);
            var user = userRepository.findUserById(userId);

            forumRating = new ForumRating();
            forumRating.setForum(forum);
            forumRating.setUser(user);
        }
        forumRating.setRating(forumRequest.getRating());

        forumRatingRepository.save(forumRating);

        return forumService.projectionToForumDTO(forumRepository.findForumWithRatingUsingId(userId));
    }

    @Transactional
    public ForumDataDTO removeRatingFromForum(Integer forumId, Integer ratingId) {
        var forumRating = forumRatingRepository.findForumRatingById(ratingId);
        forumRatingRepository.delete(forumRating);
        return forumService.projectionToForumDTO(forumRepository.findForumWithRatingUsingId(forumId));
    }
}
