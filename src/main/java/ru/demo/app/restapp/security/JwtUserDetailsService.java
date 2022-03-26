package ru.demo.app.restapp.security;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.security.jwt.JwtUser;
import ru.demo.app.restapp.security.jwt.JwtUserFactory;
import ru.demo.app.restapp.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
    Optional<User> user = userService.findByName(userName);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User with username: " + userName + " not found");
    }
    JwtUser jwtUser = JwtUserFactory.create(user.get());
    log.info("IN loadUserByUsername - user with username: {} successfully loaded", userName);
    return jwtUser;
  }
}
