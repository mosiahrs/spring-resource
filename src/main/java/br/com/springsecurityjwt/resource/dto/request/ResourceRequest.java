package br.com.springsecurityjwt.resource.dto.request;

import br.com.springsecurityjwt.resource.dto.TagDTO;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public class ResourceRequest implements Serializable {
    @NotBlank
    private String name;
    private List<TagDTO> tags;

    public ResourceRequest() {
    }

    public ResourceRequest(String name, List<TagDTO> tags) {
        this.name = name;
        this.tags = tags;
    }

    public ResourceRequest(String name) {
        this.name = name;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
