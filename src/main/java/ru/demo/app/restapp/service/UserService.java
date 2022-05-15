package ru.demo.app.restapp.service;

import org.springframework.http.ResponseEntity;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.controller.UsersApi;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface UserService extends UsersApi {

  @Nonnull
  ResponseEntity<UserFullResponse> findById(Long id);

  ResponseEntity<Void> createUser(@Nonnull UserRequest request);

  @Nonnull
  ResponseEntity<UserFullResponse> updateUserEmail(@Nonnull ChangeEmailRequest request);

  ResponseEntity<Void> deleteUser(@Nullable Long id);

  ResponseEntity<List<UserFullResponse>> findAll(@Nullable Integer age, @Nullable String phone, @Nullable String name,
      @Nullable String email, @Nullable Integer page, @Nullable Integer size);

  Optional<User> findByName(String name);

  Optional<User> findByUserName(String username);
}
