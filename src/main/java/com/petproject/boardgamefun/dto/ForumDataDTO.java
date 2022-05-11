package com.petproject.boardgamefun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumDataDTO {
    private ForumDTO forum;
    private Double rating;
}
