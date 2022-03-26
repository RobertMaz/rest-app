package ru.demo.app.restapp.service;

import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.model.ProfileRequest;
import ru.demo.app.restapp.repository.ProfileRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;
  private final JdbcTemplate template;

  @Override
  @Nullable
  public Profile save(@Nullable ProfileRequest profileRequest, User user) {
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
        "update profiles p set cash = cash * 1.1 where cash * 1.1 < p.initial_value * 1.07");
    log.info("Updated {} rows", updated);
  }
}
