package ru.demo.app.restapp.model;

import lombok.Builder;
import lombok.Data;
import ru.demo.app.restapp.domain.User;

@Data
@Builder
public class UserShortResponse {

  private Long id;
  private String name;
  private String email;
  private Integer age;

  public static UserShortResponse from(User user) {
    return builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .age(user.getAge())
        .build();
  }
}
