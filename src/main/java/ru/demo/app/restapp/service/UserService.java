package ru.demo.app.restapp.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.model.UserFullResponse;
import ru.demo.app.restapp.model.UserRequest;

@Service
public interface UserService {

  @Nonnull
  UserFullResponse getById(Long id);

  Long create(@Nonnull UserRequest request);

  @Nonnull
  UserFullResponse update(Long id, @Nonnull UserRequest request);

  void delete(Long id);

  List<UserFullResponse> findAll(Optional<Integer> age, Optional<String> phone,
      Optional<String> name, Optional<String> email, Optional<Integer> page,
      Optional<Integer> size);

  Optional<User> findByName(String userName);
}
