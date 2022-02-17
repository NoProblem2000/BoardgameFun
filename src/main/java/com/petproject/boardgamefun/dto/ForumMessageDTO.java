package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ForumMessageDTO {
    private Integer id;
    private String message;
    private OffsetDateTime messageTime;
    private User user;
}
