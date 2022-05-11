package com.petproject.boardgamefun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameDataDTO {
    private GameDTO game;
    private Double rating;
    private List<String> designers;
}
