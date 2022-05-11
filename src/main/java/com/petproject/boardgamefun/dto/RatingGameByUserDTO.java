package com.petproject.boardgamefun.dto;

import java.io.Serializable;

public record RatingGameByUserDTO(Integer id, Double rating) implements Serializable {
}
