package br.com.springsecurityjwt.resource.service;

import br.com.springsecurityjwt.resource.model.Resource;
import br.com.springsecurityjwt.resource.model.Tag;
import br.com.springsecurityjwt.resource.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Captor
    private ArgumentCaptor<List<Tag>> tagsCaptor;

    private Resource resource;
    private List<Tag> tags;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        setResourceAndTags();
    }

    @Test
    public void testDeleteTagsByResourceId() {
        resource.setTags(tags);

        String valuesToDelete = "tag1,tag2";

        doNothing().when(tagRepository).deleteAll(anyList());

       tagService.deleteTagsByResourceId(resource, valuesToDelete);

        verify(tagRepository).deleteAll(tagsCaptor.capture());
        List<Tag> capturedTags = tagsCaptor.getValue();

        List<String> capturedTagValues = capturedTags.stream()
                                            .map(Tag::getValue)
                                            .toList();

        assertThat(capturedTagValues).containsExactly("tag1", "tag2");

        List<Tag> remainingTags = resource.getTags();
        assertThat(remainingTags).extracting(Tag::getValue).containsExactly("tag3");
    }

    @Test
    public void testDeleteTagsByResourceId_noTagsToDelete() {
        resource.setTags(tags);

        String valuesToDelete = "tag4";

        tagService.deleteTagsByResourceId(resource, valuesToDelete);

        verify(tagRepository, never()).deleteAll(anyList());

        List<Tag> remainingTags = resource.getTags();
        assertThat(remainingTags).extracting(Tag::getValue).containsExactly("tag1", "tag2", "tag3");
    }

    private void setResourceAndTags() {
        resource = new Resource("ResourceTest",null);
        tags = Arrays.asList(
                new Tag(1L,"tag1"),
                new Tag(2L,"tag2"),
                new Tag(3L,"tag3")
        );
    }
}