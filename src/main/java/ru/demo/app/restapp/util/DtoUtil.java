package ru.demo.app.restapp.util;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.dto.PhoneDto;
import ru.demo.app.restapp.web.dto.ProfileDto;
import ru.demo.app.restapp.web.dto.UserFullResponse;

public class DtoUtil {

  private DtoUtil() {
  }

  @Nonnull
  public static UserFullResponse createUserFullResponse(User user) {
    List<Phone> phones = user.getPhones();
    List<PhoneDto> phonesResponse = null;
    if (phones != null) {
      phonesResponse = phones.stream().map(DtoUtil::createPhoneDto).collect(Collectors.toList());
    }

    return new UserFullResponse()
        .id(user.getId())
        .name(user.getName())
        .age(user.getAge())
        .email(user.getEmail())
        .phones(phonesResponse)
        .profile(createProfileDto(user.getProfile()));
  }

  @Nullable
  public static ProfileDto createProfileDto(@Nullable Profile profile) {
    if (profile == null) {
      return null;
    }
    return new ProfileDto().id(profile.getId()).cash(profile.getCash());
  }

  public static PhoneDto createPhoneDto(Phone phone) {
    return new PhoneDto().value(phone.getValue()).id(phone.getId());
  }
}
