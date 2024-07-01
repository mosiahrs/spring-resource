package br.com.springsecurityjwt.framework.config.authentication;

import br.com.springsecurityjwt.resource.model.TBUser;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthenticated implements UserDetails {
  private final TBUser TBUser;

  public UserAuthenticated(TBUser TBUser) {
    this.TBUser = TBUser;
  }

  @Override
  public String getUsername() {
    return TBUser.getUsername();
  }

  @Override
  public String getPassword() {
    return TBUser.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> "read");
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
