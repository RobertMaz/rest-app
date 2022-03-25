package ru.demo.app.restapp.model;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;

@Data
@Builder
public class UserFullResponse {

  private Long id;
  private String name;
  private Integer age;
  private String email;
  private List<PhoneResponse> phones;
  private Profile profile;

  public static UserFullResponse from(User user) {
    List<Phone> phones = user.getPhones();
    List<PhoneResponse> phonesResponse = null;
    if (phones != null) {
      phonesResponse = phones.stream().map(PhoneResponse::from)
                             .collect(Collectors.toList());
    }
    return builder()
        .id(user.getId())
        .name(user.getName())
        .age(user.getAge())
        .email(user.getEmail())
        .phones(phonesResponse)
        .profile(user.getProfile().get(0))
        .build();
  }
}
