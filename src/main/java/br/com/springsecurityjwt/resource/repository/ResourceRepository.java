package br.com.springsecurityjwt.resource.repository;

import br.com.springsecurityjwt.resource.model.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> findByName(String name);

    @Query("""
            SELECT r
                FROM Resource r
            LEFT JOIN r.tags t
            WHERE (:name IS NULL OR r.name = :name)
                AND (:value IS NULL OR t.value = :value)
    """)
    Page<Resource> findAllByNameOrTags(String name, String value, Pageable pageable);

    boolean existsByName(String name);
}
