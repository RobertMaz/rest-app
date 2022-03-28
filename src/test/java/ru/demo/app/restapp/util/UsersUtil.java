package ru.demo.app.restapp.util;

import ru.demo.app.restapp.domain.User;

public class UsersUtil {

  public static User buildUser(Long id) {
    return new User()
        .setId(id)
        .setName("name")
        .setUsername("user")
        .setPassword("password")
        .setEmail("email");
  }

}
