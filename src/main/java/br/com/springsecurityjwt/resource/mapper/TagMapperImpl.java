package br.com.springsecurityjwt.resource.mapper;

import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.model.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagMapperImpl implements TagMapper{
    @Override
    public List<Tag> fromDTOToEntityTag(List<TagDTO> tagsDTO) {
        return tagsDTO.stream()
                .map(tagRequest -> new Tag(tagRequest.getValue()))
                .toList();
    }

    @Override
    public List<TagDTO> fromEntityToDTO(List<Tag> tags) {
        return tags.stream()
                .map(t -> new TagDTO(t.getValue()))
                .toList();
    }
}
