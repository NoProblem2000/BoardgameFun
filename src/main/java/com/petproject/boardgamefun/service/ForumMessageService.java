package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.ForumMessageDTO;
import com.petproject.boardgamefun.model.ForumMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForumMessageService {
    public ForumMessageDTO entityToForumMessageDTO(ForumMessage forumMessage) {
        return new ForumMessageDTO(forumMessage.getId(), forumMessage.getComment(), forumMessage.getTime(), forumMessage.getUser());
    }

    public List<ForumMessageDTO> entitiesToForumMessagesDTO(List<ForumMessage> forumMessages) {
        ArrayList<ForumMessageDTO> forumMessagesDTO = new ArrayList<>();
        for (var forumMessage :
                forumMessages) {
            forumMessagesDTO.add(new ForumMessageDTO(forumMessage.getId(), forumMessage.getComment(), forumMessage.getTime(), forumMessage.getUser()));
        }
        return forumMessagesDTO;
    }
}
