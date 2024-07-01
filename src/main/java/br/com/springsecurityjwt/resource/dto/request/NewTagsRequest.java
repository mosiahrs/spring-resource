package br.com.springsecurityjwt.resource.dto.request;

import br.com.springsecurityjwt.resource.dto.TagDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NewTagsRequest(
        @NotNull @NotEmpty List<TagDTO> tags
) {
}
