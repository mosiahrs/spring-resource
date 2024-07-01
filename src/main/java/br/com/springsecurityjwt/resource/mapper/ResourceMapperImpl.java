package br.com.springsecurityjwt.resource.mapper;

import br.com.springsecurityjwt.resource.dto.TagDTO;
import br.com.springsecurityjwt.resource.dto.request.ResourceRequest;
import br.com.springsecurityjwt.resource.dto.response.ResourceResponse;
import br.com.springsecurityjwt.resource.model.Resource;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ResourceMapperImpl implements ResourceMapper{

    @Override
    public Resource requestToResource(ResourceRequest request) {
        if (request == null) {
            return null;
        }
        return new Resource(request.getName(), null );
    }

    @Override
    public ResourceResponse resourceToResponse(Resource resource) {
        if (resource == null) {
            return null;
        }

        return new ResourceResponse(resource.getName(),
                                    resource.getTags().stream().map(tag -> new TagDTO(tag.getValue())).toList(),
                                    resource.getCreateDate(),
                                    resource.getUpdateDate());
    }

    @Override
    public List<ResourceResponse> resourceToListResponse(List<Resource> resources) {
        return resources.stream()
                .map(this::resourceToResponse)
                .toList();
    }

}