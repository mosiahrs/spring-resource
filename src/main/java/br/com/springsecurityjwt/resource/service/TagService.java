package br.com.springsecurityjwt.resource.service;

import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.mapper.TagMapperImpl;
import br.com.springsecurityjwt.resource.model.Resource;
import br.com.springsecurityjwt.resource.model.Tag;
import br.com.springsecurityjwt.resource.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapperImpl tagMapper;

    public TagService(TagRepository tagRepository, TagMapperImpl tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    public List<Tag> createTagsFromDTO(List<TagDTO> tagsDTO) {
        return saveTags(tagMapper.fromDTOToEntityTag(tagsDTO));
    }

    public Tag createTagFromDTO(TagDTO tagDTO) {
        Tag tag = new Tag(tagDTO.getValue());
        return tagRepository.save(tag);
    }

    public List<TagDTO> findAllTags() {
        var tags = tagRepository.findAll();
        return tagMapper.fromEntityToDTO(tags);
    }

    private List<Tag> saveTags(List<Tag> newTags) {
        return tagRepository.saveAll(newTags);
    }

    @Transactional
    public void deleteTagsByResourceId(Resource resource, String values) {
        Set<String> valuesToDelete = new HashSet<>(Arrays.asList(values.split(",")));

        List<Tag> tagsToRemove = resource.getTags().stream()
                .filter(tag -> valuesToDelete.contains(tag.getValue()))
                .toList();

        if (tagsToRemove.isEmpty()) {
            return;
        }

        List<Tag> mutableTags = new ArrayList<>(resource.getTags());
        mutableTags.removeAll(tagsToRemove);

        resource.setTags(mutableTags);
        tagRepository.deleteAll(tagsToRemove);
    }

}
