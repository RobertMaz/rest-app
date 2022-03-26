package ru.demo.app.restapp.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

  public JwtAuthenticationException(final String msg, final Throwable cause) {
    super(msg, cause);
  }

  public JwtAuthenticationException(final String msg) {
    super(msg);
  }
}
