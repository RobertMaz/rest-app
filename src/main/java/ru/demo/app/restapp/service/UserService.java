package ru.demo.app.restapp.service;

import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.model.UserFullResponse;
import ru.demo.app.restapp.model.UserRequest;
import ru.demo.app.restapp.model.UserShortResponse;

@Service
public interface UserService {

  @Nonnull
  List<UserShortResponse> findAll();

  @Nonnull
  UserFullResponse getById(Long id);

  Long create(@Nonnull UserRequest request);

  @Nonnull
  UserFullResponse update(Long id, @Nonnull UserRequest request);

  void delete(Long id);
}
