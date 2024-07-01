package br.com.springsecurityjwt.resource.mapper;

import br.com.springsecurityjwt.resource.dto.request.ResourceRequest;
import br.com.springsecurityjwt.resource.dto.response.ResourceResponse;
import br.com.springsecurityjwt.resource.model.Resource;

import java.util.List;

public interface ResourceMapper {

    Resource requestToResource(ResourceRequest request);

    ResourceResponse resourceToResponse(Resource resource);

    List<ResourceResponse> resourceToListResponse(List<Resource> resources);
}
