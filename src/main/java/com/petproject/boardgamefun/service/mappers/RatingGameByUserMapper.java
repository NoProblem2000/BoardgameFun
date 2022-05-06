package com.petproject.boardgamefun.service.mappers;

import com.petproject.boardgamefun.dto.RatingGameByUserDTO;
import com.petproject.boardgamefun.model.RatingGameByUser;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RatingGameByUserMapper {
    RatingGameByUser ratingGameByUserDTOToRatingGameByUser(RatingGameByUserDTO ratingGameByUserDTO);

    RatingGameByUserDTO ratingGameByUserToRatingGameByUserDTO(RatingGameByUser ratingGameByUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRatingGameByUserFromRatingGameByUserDTO(RatingGameByUserDTO ratingGameByUserDTO, @MappingTarget RatingGameByUser ratingGameByUser);

    RatingGameByUser ratingGameByUserDTOToRatingGameByUser1(com.petproject.boardgamefun.dto.RatingGameByUserDTO ratingGameByUserDTO);

    com.petproject.boardgamefun.dto.RatingGameByUserDTO ratingGameByUserToRatingGameByUserDTO1(RatingGameByUser ratingGameByUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRatingGameByUserFromRatingGameByUserDTO1(com.petproject.boardgamefun.dto.RatingGameByUserDTO ratingGameByUserDTO, @MappingTarget RatingGameByUser ratingGameByUser);
}
