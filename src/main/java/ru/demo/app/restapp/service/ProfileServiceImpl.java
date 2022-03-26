package ru.demo.app.restapp.service;

import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.model.ProfileRequest;
import ru.demo.app.restapp.repository.ProfileRepository;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;

  @Override
  @Nullable
  public Profile save(@Nullable ProfileRequest profileRequest, User user) {
    Profile userProfile = user.getProfile();
    if (userProfile != null && profileRequest != null) {
      userProfile.setCash(profileRequest.getCash());
    } else if (profileRequest != null) {
      userProfile = new Profile().setCash(profileRequest.getCash()).setUser(user);
    } else {
      return null;
    }
    userProfile = profileRepository.save(userProfile);
    return userProfile;
  }
}
