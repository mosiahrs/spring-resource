package br.com.springsecurityjwt.framework.config.web;

import br.com.springsecurityjwt.framework.config.web.errors.ResourceNotFoundException;
import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.dto.request.ResourceRequest;
import br.com.springsecurityjwt.resource.model.Resource;
import br.com.springsecurityjwt.resource.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/sql-for-tests/database-initialization/initialize-database.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql-for-tests/database-initialization/clear-database.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ResourceControllerTest {

    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private ResourceController resourceController;

    private String jwtToken;
    private List<TagDTO> tagsDTO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        setListTagDTO();
        getAuth();
    }

    @Test
    void testNewResource_ResourceCreated() throws Exception {
        ResourceRequest resourceRequest = new ResourceRequest("CreateTestResource", tagsDTO);

        when(resourceService.doesResourceExist(resourceRequest.getName())).thenReturn(false);

        String jsonRequest = objectMapper.writeValueAsString(resourceRequest);

        ResultActions resultActions = mockMvc.perform(post("/resources")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(jsonPath("$.name").value(resourceRequest.getName()));
    }

    @Test
    void testNewResource_shouldThrowResourceAlreadyExistsException() throws Exception {
        ResourceRequest resourceRequest = new ResourceRequest("ExistingNameTest", tagsDTO);

        String jsonRequest = objectMapper.writeValueAsString(resourceRequest);

        ResultActions resultActions = mockMvc.perform(post("/resources")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest));

        resultActions.andExpect(status().isConflict());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteResource_shouldBeReturnNoContent() throws Exception {
        Resource resource = new Resource();
        when(resourceService.findResourceIdValidated(anyLong())).thenReturn(resource);

        ResultActions resultActions =  mockMvc.perform(delete("/resources/{id}", 50L)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteResource_ResourceNotFoundException() throws Exception {
        when(resourceService.findResourceIdValidated(anyLong()))
                .thenThrow(new ResourceNotFoundException("Resource not found"));

        ResultActions resultActions = mockMvc.perform(delete("/resources/{id}", 200L)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
        Mockito.verify(resourceService, Mockito.never()).removeTagsAndDeleteResource(Mockito.any());
    }

    private void getAuth() throws Exception {
        MvcResult result = mockMvc.perform(post("/authenticate")
                        .with(httpBasic("username", "password"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        jwtToken = objectMapper.readTree(responseContent).path("token").asText();
    }

    private void setListTagDTO() {
        var tag1 = new TagDTO("Tag1");
        var tag2 = new TagDTO("Tag2");
        var tag3 = new TagDTO("Tag3");

        tagsDTO = List.of(tag1,tag2,tag3);
    }
}