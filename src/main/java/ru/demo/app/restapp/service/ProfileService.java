package ru.demo.app.restapp.service;

import javax.annotation.Nullable;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.model.ProfileRequest;

public interface ProfileService {

  Profile save(@Nullable ProfileRequest profile, User user);
}
