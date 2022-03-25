package ru.demo.app.restapp.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.demo.app.restapp.domain.Profile;

@Data
@Builder
@Accessors(chain = true)
public class ProfileResponse {

  private Long id;
  private BigDecimal cash;

  public static ProfileResponse from(Profile profile) {
    return builder()
        .id(profile.getId())
        .cash(profile.getCash())
        .build();
  }
}
