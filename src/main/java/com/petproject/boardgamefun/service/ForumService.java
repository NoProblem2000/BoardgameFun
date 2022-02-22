package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.ForumDTO;
import com.petproject.boardgamefun.dto.projection.ForumProjection;
import com.petproject.boardgamefun.model.Forum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForumService {
    public List<ForumDTO> projectionsToForumDTO(List<ForumProjection> projections) {
        ArrayList<ForumDTO> forums = new ArrayList<>();
        for (var projection : projections) {
            forums.add(new ForumDTO(projection.getForum(), projection.getRating()));
        }
        return forums;
    }

    public ForumDTO projectionToForumDTO(ForumProjection projection) {
        ForumDTO forum = new ForumDTO();
        forum.setForum(projection.getForum());
        forum.setRating(projection.getRating());
        return forum;
    }
    
    public List<ForumDTO> entitiesToForumDTO(List<Forum> forums){
        ArrayList<ForumDTO> forumsDTO = new ArrayList<>();
        for (var forum :
                forums) {
            forumsDTO.add(new ForumDTO(forum, null));
        }
        return forumsDTO;
    }
    
    public ForumDTO entityToForumDTO(Forum forum){
        return new ForumDTO(forum, null);
    }
}
