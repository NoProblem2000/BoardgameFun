package com.petproject.boardgamefun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSellDTO {
    private GameDTO game;
    private String condition;
    private String comment;
    private Integer price;
}
