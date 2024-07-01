package br.com.springsecurityjwt.resource.dto;

import java.io.Serializable;

public class TagDTO implements Serializable {
    private String value;

    public TagDTO() {
    }

    public TagDTO(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "value='" + value + '\'' +
                '}';
    }
}
