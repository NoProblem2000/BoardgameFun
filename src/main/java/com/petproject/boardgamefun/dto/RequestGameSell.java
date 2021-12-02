package com.petproject.boardgamefun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RequestGameSell {
    private Integer gameId;
    private String condition;
    private String comment;
    private Integer price;
}
