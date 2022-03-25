package ru.demo.app.restapp.model;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRequest {

  @NotEmpty(message = "{field.is.empty}")
  private String name;

  private String email;

  @Min(value = 12, message = "{field.min.value}")
  @Max(value = 85, message = "{field.max.value}")
  private Integer age;

  @NotEmpty(message = "{field.is.empty}")
  private ProfileRequest profile;
  private List<PhoneRequest> phones;
}
