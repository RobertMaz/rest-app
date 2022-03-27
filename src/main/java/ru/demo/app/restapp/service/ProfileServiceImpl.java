package ru.demo.app.restapp.service;

import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.repository.ProfileRepository;
import ru.demo.app.restapp.web.dto.ProfileDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;
  private final JdbcTemplate template;
  private static final double UPDATE_PERCENTAGE = 1.1; // 110%
  private static final double LIMIT_PERCENTAGE = 1.07; // 107%

  @Override
  @Nullable
  public Profile save(@Nullable ProfileDto profileRequest, User user) {
    Profile userProfile = user.getProfile();
    if (userProfile != null && profileRequest != null) {
      userProfile.setCash(profileRequest.getCash());
    } else if (profileRequest != null) {
      userProfile = new Profile()
          .setCash(profileRequest.getCash())
          .setInitialValue(profileRequest.getCash())
          .setUser(user);
    } else {
      return null;
    }
    userProfile = profileRepository.save(userProfile);
    return userProfile;
  }

  @Override
  public void increaseCashOfAllProfiles() {
    int updated = template.update(
        "update profiles p set cash = cash * ? where cash * ? < p.initial_value * ?",
        UPDATE_PERCENTAGE, UPDATE_PERCENTAGE, LIMIT_PERCENTAGE);
    if (updated > 0) {
      log.info("Updated {} rows", updated);
    } else {
      log.debug("No one rows was updated");
    }
  }
}
