package br.com.springsecurityjwt.resource.mapper;

import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.model.Tag;

import java.util.List;

public interface TagMapper {

    List<Tag> fromDTOToEntityTag(List<TagDTO> tagsDTO);
    List<TagDTO> fromEntityToDTO(List<Tag> tagsDTO);
}
