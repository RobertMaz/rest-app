package ru.demo.app.restapp.security.jwt;

import ru.demo.app.restapp.domain.User;

public final class JwtUserFactory {

  private JwtUserFactory() {
  }

  public static JwtUser create(User user) {
    return new JwtUser(user.getId(), user.getName());
  }
}
