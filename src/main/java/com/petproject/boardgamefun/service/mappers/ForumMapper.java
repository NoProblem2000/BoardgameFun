package com.petproject.boardgamefun.service.mappers;

import com.petproject.boardgamefun.dto.ForumDTO;
import com.petproject.boardgamefun.model.Forum;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ForumMapper {
    Forum forumDTOToForum(ForumDTO forumDTO);

    ForumDTO forumToForumDTO(Forum forum);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateForumFromForumDTO(ForumDTO forumDTO, @MappingTarget Forum forum);
}
