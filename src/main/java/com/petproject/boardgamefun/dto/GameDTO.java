package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameDTO {
    private Game game;
    private Double rating;
    private List<String> designers;
}
