package com.petproject.boardgamefun.service.mappers;

import com.petproject.boardgamefun.dto.DiaryRatingDTO;
import com.petproject.boardgamefun.model.DiaryRating;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DiaryRatingMapper {
    DiaryRating diaryRatingDTOToDiaryRating(DiaryRatingDTO diaryRatingDTO);

    DiaryRatingDTO diaryRatingToDiaryRatingDTO(DiaryRating diaryRating);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDiaryRatingFromDiaryRatingDTO(DiaryRatingDTO diaryRatingDTO, @MappingTarget DiaryRating diaryRating);
}
