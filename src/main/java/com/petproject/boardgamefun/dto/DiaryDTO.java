package com.petproject.boardgamefun.dto;

import com.petproject.boardgamefun.model.Diary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDTO {
    private Diary diary;
    private double rating;
}
