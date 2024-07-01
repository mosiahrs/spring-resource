package br.com.springsecurityjwt.resource.repository;

import java.util.Optional;

import br.com.springsecurityjwt.resource.model.TBUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<TBUser, String> {
  Optional<TBUser> findByUsername(String username);
}
