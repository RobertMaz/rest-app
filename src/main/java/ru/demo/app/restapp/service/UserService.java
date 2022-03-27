package ru.demo.app.restapp.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;

@Service
public interface UserService {

  @Nonnull
  UserFullResponse getById(Long id);

  Long create(@Nonnull UserRequest request);

  @Nonnull
  UserFullResponse updateUserEmail(String username, @Nonnull ChangeEmailRequest request);

  void delete(Long id);

  List<UserFullResponse> findAll(Optional<Integer> age, Optional<String> phone,
      Optional<String> name, Optional<String> email, Optional<Integer> page,
      Optional<Integer> size);

  Optional<User> findByName(String userName);
}
