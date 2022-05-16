package ru.demo.app.restapp.service;

import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface UserService {

  @Nonnull
  UserFullResponse findById(Long id);

  User createUser(@Nonnull UserRequest request);

  @Nonnull
  UserFullResponse updateUserEmail(@Nonnull ChangeEmailRequest request);

  void deleteUser(@Nullable Long id);

  List<UserFullResponse> findAll(@Nullable Integer age, @Nullable String phone, @Nullable String name,
      @Nullable String email, @Nullable Integer page, @Nullable Integer size);

  Optional<User> findByName(String name);

  Optional<User> findByUserName(String username);
}
