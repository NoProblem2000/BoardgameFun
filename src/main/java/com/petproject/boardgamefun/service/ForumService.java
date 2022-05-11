package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.ForumDataDTO;
import com.petproject.boardgamefun.dto.projection.ForumProjection;
import com.petproject.boardgamefun.model.Forum;
import com.petproject.boardgamefun.service.mappers.ForumMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForumService {

    final ForumMapper forumMapper;

    public ForumService(ForumMapper forumMapper) {
        this.forumMapper = forumMapper;
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
