package br.com.springsecurityjwt.resource.dto.response;

import br.com.springsecurityjwt.resource.dto.TagDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ResourceResponse(
        String name,
        List<TagDTO> tags,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {}
