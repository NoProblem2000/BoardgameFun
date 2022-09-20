package com.petproject.boardgamefun.service.mappers;

import com.petproject.boardgamefun.dto.GameSellDTO;
import com.petproject.boardgamefun.model.GameSell;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",  injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GameSellMapper {

    GameSell GameSellDTOToGameSell(GameSellDTO gameSellDTO);

    GameSellDTO GameSellToGameSellDTO(GameSell gameSell);


}
