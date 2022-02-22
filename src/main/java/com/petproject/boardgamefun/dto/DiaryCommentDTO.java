package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class DiaryCommentDTO {
    private Integer id;
    private String comment;
    private OffsetDateTime time;
    private User user;
}
