package ru.demo.app.restapp.model;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import ru.demo.app.restapp.domain.Phone;

@Data
@Builder
public class PhoneRequest {

  private Long id;

  @NotEmpty(message = "{field.is.empty}")
  private String value;

  public static PhoneRequest from(Phone phone) {
    return builder().id(phone.getId()).value(phone.getValue()).build();
  }

}
