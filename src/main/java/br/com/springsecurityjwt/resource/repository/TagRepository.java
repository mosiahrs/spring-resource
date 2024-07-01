package br.com.springsecurityjwt.resource.repository;

import br.com.springsecurityjwt.resource.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
