package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.ForumMessageDTO;
import com.petproject.boardgamefun.model.ForumMessage;
import com.petproject.boardgamefun.service.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForumMessageService {

    final UserMapper userMapper;

    public ForumMessageService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ForumMessageDTO entityToForumMessageDTO(ForumMessage forumMessage) {
        return new ForumMessageDTO(forumMessage.getId(), forumMessage.getComment(), forumMessage.getTime(), userMapper.userToUserDTO(forumMessage.getUser()));
    }

    public List<ForumMessageDTO> entitiesToForumMessagesDTO(List<ForumMessage> forumMessages) {
        ArrayList<ForumMessageDTO> forumMessagesDTO = new ArrayList<>();
        for (var forumMessage :
                forumMessages) {
            forumMessagesDTO.add(new ForumMessageDTO(forumMessage.getId(), forumMessage.getComment(), forumMessage.getTime(), userMapper.userToUserDTO(forumMessage.getUser())));
        }
        return forumMessagesDTO;
    }
}
