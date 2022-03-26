package ru.demo.app.restapp.model;

import java.math.BigDecimal;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import ru.demo.app.restapp.domain.Profile;

@Data
@Builder
public class ProfileResponse {

  private Long id;
  private BigDecimal cash;

  @Nullable
  public static ProfileResponse from(@Nullable Profile profile) {
    if (profile == null) {
      return null;
    }
    return builder()
        .id(profile.getId())
        .cash(profile.getCash())
        .build();
  }
}
