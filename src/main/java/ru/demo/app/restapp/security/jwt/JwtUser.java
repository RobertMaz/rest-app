package ru.demo.app.restapp.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class JwtUser implements UserDetails {

  private final Long id;
  private final String username;

  @JsonIgnore
  public Long getId() {
    return id;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }


  @JsonIgnore
  @Override
  public String getPassword() {
    return "$2a$10$6ySv/8p0yWk9x51by7Ntn.Uzm.i1i5/97T6okNUKbLUuswCpk5rtK";// todo
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("user"));
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
