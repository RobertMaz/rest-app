package ru.demo.app.restapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.domain.Role;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.security.jwt.JwtTokenProvider;
import ru.demo.app.restapp.web.dto.AccessDto;
import ru.demo.app.restapp.web.dto.AuthenticationRequestDto;
import ru.demo.app.restapp.web.dto.UserRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;

  public AccessDto auth(AuthenticationRequestDto requestDto) {
    try {
      log.info("Trying to auth user '{}'", requestDto.getUsername());

      String username = requestDto.getUsername();
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
      User user = userService
              .findByUserName(username)
              .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username: %s not found", username)));
      List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
      String token = jwtTokenProvider.createToken(username, roles);

      AccessDto accessDto = new AccessDto();
      accessDto.setUsername(username);
      accessDto.setToken(token);
      log.debug("Access to auth user '{}'", requestDto.getUsername());
      return accessDto;
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  public void registerNewUser(UserRequest userRequest) {
    log.info("Trying to register user: '{}'", userRequest.getUsername());
    userService.createUser(userRequest);
  }
}
