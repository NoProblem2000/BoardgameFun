package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSellDTO {
    private Game game;
    private String condition;
    private String comment;
    private Integer price;
}
