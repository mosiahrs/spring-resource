package br.com.springsecurityjwt.resource.dto;

import br.com.springsecurityjwt.resource.dto.response.ResourceResponse;

import java.util.List;

public record PageResponse(
        List<ResourceResponse> content,
        Long totalElements,
        Integer totalPages,
        Boolean last,
        Integer numberOfElements,
        Boolean first
) {
}
