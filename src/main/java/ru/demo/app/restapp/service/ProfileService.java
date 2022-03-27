package ru.demo.app.restapp.service;

import javax.annotation.Nullable;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.dto.ProfileDto;

public interface ProfileService {

  Profile save(@Nullable ProfileDto profile, User user);

  void increaseCashOfAllProfiles();
}
