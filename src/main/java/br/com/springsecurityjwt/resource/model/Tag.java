package br.com.springsecurityjwt.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Resource> resources;

    public Tag() {}

    public Tag(String value) {
        this.value = value;
    }

    public Tag(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "value='" + value + '\'' +
                ", id=" + id +
                '}';
    }
}
