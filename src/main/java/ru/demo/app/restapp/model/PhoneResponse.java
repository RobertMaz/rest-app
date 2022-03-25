package ru.demo.app.restapp.model;

import lombok.Builder;
import lombok.Data;
import ru.demo.app.restapp.domain.Phone;

@Data
@Builder
public class PhoneResponse {

  private Long id;
  private String value;

  public static PhoneResponse from(Phone phone) {
    return PhoneResponse.builder()
                        .value(phone.getValue())
                        .id(phone.getId())
                        .build();
  }
}
