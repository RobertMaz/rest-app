package ru.demo.app.restapp.util;

import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.dto.UserFullResponse;

public class UsersUtil {

  public static User buildUser(Long id) {
    return new User()
        .setId(id)
        .setName("name")
        .setUsername("user")
        .setPassword("password")
        .setEmail("email");
  }

  public static User buildUser(String name) {
    return buildUser(0L)
        .setName(name)
        .setUsername(name);
  }

  public static UserFullResponse buildUserFullResponse(Long id) {
    UserFullResponse user = new UserFullResponse();
    user.setId(id);
    user.setName("name");
    user.setEmail("email");
    return user;
  }
}
