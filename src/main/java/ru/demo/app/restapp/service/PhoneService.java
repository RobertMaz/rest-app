package ru.demo.app.restapp.service;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.model.PhoneRequest;

public interface PhoneService {

  @Nonnull
  List<Phone> saveAll(@Nullable List<PhoneRequest> phonesReq, User user);
}
