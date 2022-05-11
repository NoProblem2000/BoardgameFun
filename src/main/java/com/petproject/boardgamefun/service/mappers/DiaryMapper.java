package com.petproject.boardgamefun.service.mappers;

import com.petproject.boardgamefun.dto.DiaryDTO;
import com.petproject.boardgamefun.model.Diary;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DiaryMapper {
    Diary diaryDTOToDiary(DiaryDTO diaryDTO);

    DiaryDTO diaryToDiaryDTO(Diary diary);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDiaryFromDiaryDTO(DiaryDTO diaryDTO, @MappingTarget Diary diary);
}
