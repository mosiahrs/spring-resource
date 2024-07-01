package br.com.springsecurityjwt.framework.config.web;

import br.com.springsecurityjwt.resource.dto.PageResponse;
import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.dto.request.NewTagsRequest;
import br.com.springsecurityjwt.resource.dto.request.ResourceRequest;
import br.com.springsecurityjwt.resource.dto.response.ResourceResponse;
import br.com.springsecurityjwt.resource.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> listResource(@RequestParam int page, @RequestParam int size,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String tag) {
        var response = resourceService.findAllResources(name, tag, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> newResource(@RequestBody @Validated ResourceRequest resourceRequest) {
        var response = resourceService.createResourceFromDTO(resourceRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        var resource = resourceService.findResourceIdValidated(id);
        resourceService.removeTagsAndDeleteResource(resource);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<ResourceResponse> newTags(@PathVariable Long id, @Validated @RequestBody NewTagsRequest request) {
        var resource = resourceService.findResourceIdValidated(id);
        var response = resourceService.createNewTagsToResource(request.tags(), resource);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}/tags")
    public ResponseEntity<List<TagDTO>> deleteTag(@PathVariable Long id, @RequestParam String tag) {
        var resource = resourceService.findResourceIdValidated(id);
        var response = resourceService.removeTagsByNameAndListAll(resource, tag);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}