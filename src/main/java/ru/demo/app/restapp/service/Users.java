package ru.demo.app.restapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.demo.app.restapp.web.controller.UsersApiDelegate;
import ru.demo.app.restapp.web.dto.UserFullResponse;

@Service
public class Users implements UsersApiDelegate {

  @Override
  public ResponseEntity<List<UserFullResponse>> getUsers(Optional<Integer> age, Optional<String> phone,
      Optional<String> name, Optional<String> email, Optional<Integer> page, Optional<Integer> size) {
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
  }
}
