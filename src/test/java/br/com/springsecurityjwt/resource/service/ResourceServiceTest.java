package br.com.springsecurityjwt.resource.service;

import br.com.springsecurityjwt.framework.config.web.errors.ResourceNotFoundException;
import br.com.springsecurityjwt.framework.config.web.errors.TagAlreadyExistsException;
import br.com.springsecurityjwt.resource.dto.PageResponse;
import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.dto.response.ResourceResponse;
import br.com.springsecurityjwt.resource.mapper.ResourceMapperImpl;
import br.com.springsecurityjwt.resource.model.Resource;
import br.com.springsecurityjwt.resource.model.Tag;
import br.com.springsecurityjwt.resource.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceServiceTest {

    @Mock
    private ResourceRepository mockResourceRepository;

    @Mock
    private TagService mockTagService;

    @Mock
    private ResourceMapperImpl mockResourceMapper;

    @InjectMocks
    private ResourceService resourceService;

    private Resource resource;
    private List<Tag> tags;
    private List<TagDTO> tagsDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        resource = new Resource();
        resource.setId(1L);
        resource.setName("Resource Test");

        initializeTags();
    }

    @Test
    void testFindAllResources_Success() {
        String name = "test";
        String tag = "tag";
        int page = 0;
        int size = 10;

        var resources = getListResource();

        Page<Resource> resourcePage = new PageImpl<>(resources);

        when(mockResourceRepository.findAllByNameOrTags(eq(name), eq(tag), any(PageRequest.class))).thenReturn(resourcePage);

        List<ResourceResponse> expectedResponses = getListResourceResponse();
        when(mockResourceMapper.resourceToListResponse(resourcePage.getContent())).thenReturn(expectedResponses);

       PageResponse actualResponses = resourceService.findAllResources(name, tag, page, size);

        assertFalse(actualResponses.content().isEmpty());
        assertEquals(expectedResponses.size(), actualResponses.content().size());
        assertEquals(expectedResponses.get(0).name(), actualResponses.content().get(0).name());
        assertEquals(expectedResponses.get(1).name(), actualResponses.content().get(1).name());

        verify(mockResourceRepository, times(1)).findAllByNameOrTags(eq(name), eq(tag), any(PageRequest.class));
        verify(mockResourceMapper, times(1)).resourceToListResponse(resourcePage.getContent());
    }

    @Test
    void testRemoveTagsAndDeleteResource() {
        resource.setTags(new ArrayList<>(tags));

        when(mockTagService.findAllTags()).thenReturn(tagsDTO);
        resourceService.removeTagsAndDeleteResource(resource);

        verify(mockResourceRepository, times(1)).save(resource);
        verify(mockResourceRepository, times(1)).delete(resource);
    }

    @Test
    void testThrowResourceNotFoundExceptionWhenNoResourcesFound() {
        String name = "nonexistent";
        String tag = "notag";
        int page = 0;
        int size = 10;

        when(mockResourceRepository.findAllByNameOrTags(eq(name), eq(tag), any(PageRequest.class)))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.findAllResources(name, tag, page, size));

        verify(mockResourceRepository, times(1)).findAllByNameOrTags(eq(name), eq(tag), any(PageRequest.class));
        verify(mockResourceMapper, never()).resourceToListResponse(anyList());
    }

    @Test
    void testRemoveTagsByNameAndListAll() {
        resourceService.removeTagsByNameAndListAll(resource, "Tag1,Tag2");
        verify(mockTagService, times(1)).deleteTagsByResourceId(resource, "Tag1,Tag2");
        verify(mockResourceRepository, times(1)).save(resource);
        when(mockTagService.findAllTags()).thenReturn(tagsDTO);
    }

    @Test
    public void testUpdateResourceWithTags_NewTags() {
        TagDTO tagDTO4 = new TagDTO("tag4");
        TagDTO tagDTO5 = new TagDTO("tag5");
        List<TagDTO> tags = Arrays.asList(tagDTO4, tagDTO5);

        Tag tag4 = new Tag("tag4");
        Tag tag5 = new Tag("tag5");

        when(mockTagService.createTagFromDTO(tagDTO4)).thenReturn(tag4);
        when(mockTagService.createTagFromDTO(tagDTO5)).thenReturn(tag5);

        resourceService.updateResourceWithTags(tags, resource);
        resource.setTags(List.of(tag4,tag5));

        assertEquals(2, resource.getTags().size());
        verify(mockTagService, times(1)).createTagFromDTO(tagDTO4);
        verify(mockTagService, times(1)).createTagFromDTO(tagDTO5);
    }

    @Test
    public void testUpdateResource_WithExistingTags() {
        when(mockTagService.createTagsFromDTO(tagsDTO)).thenReturn(tags);

        Set<Tag> existingTags = new LinkedHashSet<>();
        existingTags.add(new Tag("tag1"));

        resource.setTags(new ArrayList<>(existingTags));

        assertThrows(TagAlreadyExistsException.class,
                () -> resourceService.updateResourceWithTags(tagsDTO, resource));
    }

    @Test
    void testFindResourceById() {
        Long resourceId = 1L;
        Resource mockResource = new Resource(resourceId, "Test Resource", tags, LocalDateTime.now(), LocalDateTime.now());

        when(mockResourceRepository.findById(resourceId))
                .thenReturn(Optional.of(mockResource));

        Optional<Resource> foundResource = resourceService.findResourceById(resourceId);

        assertTrue(foundResource.isPresent());
        assertEquals(mockResource, foundResource.get());

        verify(mockResourceRepository, times(1)).findById(resourceId);
    }

    @Test
    void testDoesResourceExist() {
        String existingResourceName = "Test Resource";
        String nonExistingResourceName = "Nonexistent Resource";

        when(mockResourceRepository.existsByName(existingResourceName)).thenReturn(true);
        when(mockResourceRepository.existsByName(nonExistingResourceName)).thenReturn(false);

        boolean resultExisting = resourceService.doesResourceExist(existingResourceName);
        boolean resultNonExisting = resourceService.doesResourceExist(nonExistingResourceName);

        assertTrue(resultExisting);
        assertFalse(resultNonExisting);

        verify(mockResourceRepository, times(1)).existsByName(existingResourceName);
        verify(mockResourceRepository, times(1)).existsByName(nonExistingResourceName);
    }

    private void initializeTags() {
        tags = new ArrayList<>();
        Tag tag1 = new Tag(1L,"tag1");
        tag1.setResources(new ArrayList<>(List.of(resource)));
        Tag tag2 = new Tag(2L,"tag2");
        tag2.setResources(new ArrayList<>(List.of(resource)));
        tags.add(tag1);
        tags.add(tag2);

        tagsDTO =  List.of(new TagDTO("tag1"), new TagDTO("tag2"));
    }

    private List<Resource> getListResource(){
        Resource resource1 = new Resource(1L, "Resource 1", tags, LocalDateTime.now(), LocalDateTime.now());
        Resource resource2 = new Resource(2L, "Resource 2", tags, LocalDateTime.now(), LocalDateTime.now());
        return List.of(resource1, resource2);
    }

    private List<ResourceResponse> getListResourceResponse(){
        ResourceResponse resource1 = new ResourceResponse( "Resource 1", tagsDTO, LocalDateTime.now(), LocalDateTime.now());
        ResourceResponse resource2 = new ResourceResponse( "Resource 2", tagsDTO, LocalDateTime.now(), LocalDateTime.now());
        return List.of(resource1, resource2);
    }
}
