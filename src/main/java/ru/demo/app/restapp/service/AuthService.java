package ru.demo.app.restapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.demo.app.restapp.domain.Role;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.security.jwt.JwtTokenProvider;
import ru.demo.app.restapp.web.controller.AuthApiDelegate;
import ru.demo.app.restapp.web.dto.AccessDto;
import ru.demo.app.restapp.web.dto.AuthenticationRequestDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService implements AuthApiDelegate {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;

  @Override
  public ResponseEntity<AccessDto> auth(AuthenticationRequestDto requestDto) {
    try {
      log.info("Trying to auth user '{}'", requestDto.getUsername());

      String username = requestDto.getUsername();
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
      Optional<User> user = userService.findByUserName(username);
      if (user.isEmpty()) {
        throw new UsernameNotFoundException("User with username: " + username + " not found");
      }
      List<String> roles = user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList());
      String token = jwtTokenProvider.createToken(username, roles);

      AccessDto accessDto = new AccessDto();
      accessDto.setUsername(username);
      accessDto.setToken(token);
      log.debug("Access to auth user '{}'", requestDto.getUsername());
      return ResponseEntity.ok(accessDto);
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }
}
