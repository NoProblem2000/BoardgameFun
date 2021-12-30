package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Forum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumDTO {
    private Forum forum;
    private Double rating;
}
