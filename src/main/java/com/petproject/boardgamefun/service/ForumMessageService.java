package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.ForumMessageDTO;
import com.petproject.boardgamefun.dto.request.ForumMessageRequest;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.ForumMessage;
import com.petproject.boardgamefun.repository.ForumMessageRepository;
import com.petproject.boardgamefun.repository.ForumRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.service.mappers.UserMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ForumMessageService {

    final UserMapper userMapper;
    final ForumMessageRepository forumMessageRepository;
    final ForumRepository forumRepository;
    final UserRepository userRepository;

    public ForumMessageService(UserMapper userMapper, ForumMessageRepository forumMessageRepository, ForumRepository forumRepository, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.forumMessageRepository = forumMessageRepository;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<ForumMessageDTO> getMessages(Integer forumId, Integer userId) {
        List<ForumMessage> messages;
        if (forumId != null)
            messages = forumMessageRepository.findByForumId(forumId);
        else if (userId != null)
            messages = forumMessageRepository.findByUserId(userId);
        else
            messages = forumMessageRepository.findAll();

        return entitiesToForumMessagesDTO(messages);
    }

    @Transactional
    public List<ForumMessageDTO> addMessage(Integer forumId, Integer userId, ForumMessageRequest forumMessageRequest) {
        var forum = forumRepository.findForumById(forumId);
        var user = userRepository.findUserById(userId);

        var forumMessage = new ForumMessage();
        forumMessage.setForum(forum);
        forumMessage.setUser(user);
        forumMessage.setTime(OffsetDateTime.now());
        forumMessage.setComment(forumMessageRequest.getComment());

        forumMessageRepository.save(forumMessage);

        var messages = forumMessageRepository.findByForumId(forumId);
        return entitiesToForumMessagesDTO(messages);
    }

    @Transactional
    public List<ForumMessageDTO> updateMessage(Integer messageId, Integer forumId, ForumMessageRequest forumMessageRequest) {
        var message = forumMessageRepository.findForumMessageById(messageId);
        if (message == null) {
            throw new NoRecordFoundException("Message not found");
        }

        if (!Objects.equals(forumMessageRequest.getComment(), message.getComment()))
            message.setComment(forumMessageRequest.getComment());

        forumMessageRepository.save(message);
        return entitiesToForumMessagesDTO(forumMessageRepository.findByForumId(forumId));
    }

    @Transactional
    public List<ForumMessageDTO> deleteMessage(Integer forumId, Integer messageId) {
        var message = forumMessageRepository.findForumMessageById(messageId);
        forumMessageRepository.delete(message);
        return entitiesToForumMessagesDTO(forumMessageRepository.findByForumId(forumId));
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
