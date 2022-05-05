package com.petproject.boardgamefun.service.mappers;

import com.petproject.boardgamefun.dto.UserDTO;
import com.petproject.boardgamefun.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    User userDTOToUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserDTO(UserDTO userDTO, @MappingTarget User user);
}
