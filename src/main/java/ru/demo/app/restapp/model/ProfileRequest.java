package ru.demo.app.restapp.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.demo.app.restapp.domain.Profile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

  private BigDecimal cash;

  public static ProfileRequest from(Profile profile) {
    return builder().cash(profile.getCash()).build();
  }
}
