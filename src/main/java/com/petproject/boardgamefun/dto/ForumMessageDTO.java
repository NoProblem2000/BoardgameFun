package com.petproject.boardgamefun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ForumMessageDTO {
    private Integer id;
    private String message;
    private OffsetDateTime messageTime;
}
