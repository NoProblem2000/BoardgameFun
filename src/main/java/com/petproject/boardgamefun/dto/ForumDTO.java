package com.petproject.boardgamefun.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

public record ForumDTO(Integer id, String title, String text, OffsetDateTime publicationTime, GameDTO game,
                       UserDTO user) implements Serializable {
}
