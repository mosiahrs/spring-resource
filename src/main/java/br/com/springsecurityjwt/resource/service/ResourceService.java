package br.com.springsecurityjwt.resource.service;

import br.com.springsecurityjwt.framework.config.web.errors.InvalidTagInputException;
import br.com.springsecurityjwt.framework.config.web.errors.ResourceAlreadyExistsException;
import br.com.springsecurityjwt.framework.config.web.errors.ResourceNotFoundException;
import br.com.springsecurityjwt.framework.config.web.errors.TagAlreadyExistsException;
import br.com.springsecurityjwt.resource.dto.PageResponse;
import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.dto.request.ResourceRequest;
import br.com.springsecurityjwt.resource.dto.response.ResourceResponse;
import br.com.springsecurityjwt.resource.mapper.ResourceMapperImpl;
import br.com.springsecurityjwt.resource.model.Resource;
import br.com.springsecurityjwt.resource.model.Tag;
import br.com.springsecurityjwt.resource.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static br.com.springsecurityjwt.resource.utilz.UtilsValidator.validateTagsUpdate;

@Service
public class ResourceService {

    private static final Logger log = LoggerFactory.getLogger(ResourceService.class);

    private final ResourceRepository resourceRepository;
    private final TagService tagService;
    private final ResourceMapperImpl resourceMapper;

    public ResourceService(ResourceRepository resourceRepository, TagService tagService, ResourceMapperImpl resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.tagService = tagService;
        this.resourceMapper = resourceMapper;
    }

    private Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource findResourceByName(String name) {
        return resourceRepository.findByName(name).orElse(null);
    }

    public Resource findResourceIdValidated(Long id) {
        return findResourceById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
    }

    public ResourceResponse createResourceFromDTO(ResourceRequest request) {
        if (doesResourceExist(request.getName())) {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }

        var resource = resourceMapper.requestToResource(request);
        updateResourceWithTags(request.getTags(), resource);
        Resource savedResource = saveResource(resource);

        return resourceMapper.resourceToResponse(savedResource);
    }

    public ResourceResponse createNewTagsToResource(List<TagDTO> tags, Resource resource) {
        updateResourceWithTags(tags, resource);
        Resource savedResource = saveResource(resource);

        return resourceMapper.resourceToResponse(savedResource);
    }

    public void updateResourceWithTags(List<TagDTO> tags, Resource resource) {
        Set<Tag> savedTags = new LinkedHashSet<>();
        if (resource.getTags() != null) {
            savedTags.addAll(resource.getTags());
        }

        for (TagDTO newTag : tags) {
            boolean exists = savedTags.stream().anyMatch(tag -> tag != null && tag.getValue().equals(newTag.getValue()));
            if (!exists) {
                var tagToSave = tagService.createTagFromDTO(newTag);
                savedTags.add(tagToSave);
            } else {
                String message = "Tag '" + newTag.getValue() + "' already exists in this Resource '" + resource.getName() + "'";
                throw new TagAlreadyExistsException(message);
            }
        }

        resource.setTags(new ArrayList<>(savedTags));
    }

    public boolean doesResourceExist(String name) {
        return resourceRepository.existsByName(name);
    }

    public PageResponse findAllResources(String name, String tag, int page, int size) {
        var resourcePage = resourceRepository.findAllByNameOrTags(name, tag, createPageRequest(page, size));
        if (resourcePage == null || resourcePage.getTotalElements() == 0) {
            throw new ResourceNotFoundException("Resource not found");
        }

        return new PageResponse(
                resourceMapper.resourceToListResponse(resourcePage.getContent())
                , resourcePage.getTotalElements()
                , resourcePage.getTotalPages()
                , resourcePage.isLast()
                , resourcePage.getNumberOfElements()
                , resourcePage.isFirst());
    }

    private PageRequest createPageRequest(int page, int size) {
        return PageRequest.of(page, size, Sort.by("updateDate").descending());
    }

    @Transactional
    public void removeTagsAndDeleteResource(Resource resource) {
        try {
            if (resource.getTags() != null) {
                for (Tag tag : resource.getTags()) {
                    tag.getResources().remove(resource);
                }
            }
            saveResource(resource);
            deleteResource(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteResource(Resource resource) {
        resourceRepository.delete(resource);
    }

    public List<TagDTO> removeTagsByNameAndListAll(Resource resource, String values) {
        if (!validateTagsUpdate(values)) {
            throw new InvalidTagInputException("The values " + values + " does not match the expected pattern.");
        }
        tagService.deleteTagsByResourceId(resource, values);
        saveResource(resource);

        return tagService.findAllTags();
    }

    public Optional<Resource> findResourceById(Long id) {
        return resourceRepository.findById(id);
    }
}
