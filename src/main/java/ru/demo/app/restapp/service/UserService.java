package ru.demo.app.restapp.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.controller.UsersApiDelegate;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;

public interface UserService extends UsersApiDelegate {

  @Nonnull
  ResponseEntity<UserFullResponse> findById(Long id);

  ResponseEntity<Void> createUser(@Nonnull UserRequest request);

  @Nonnull
  ResponseEntity<UserFullResponse> updateUserEmail(@Nonnull ChangeEmailRequest request);

  ResponseEntity<Void> deleteUser(Long id);

  ResponseEntity<List<UserFullResponse>> findAll(Integer age, String phone, String name, String email, Integer page,
      Integer size);

  Optional<User> findByName(String name);

  Optional<User> findByUserName(String username);
}
