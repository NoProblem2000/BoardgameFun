package com.petproject.boardgamefun.service.mappers;

import com.petproject.boardgamefun.dto.GameDTO;
import com.petproject.boardgamefun.model.Game;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",  injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GameMapper {
    Game gameDTOToGame(GameDTO gameDTO);

    GameDTO gameToGameDTO(Game game);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGameFromGameDTO(GameDTO gameDTO, @MappingTarget Game game);
}
