package ru.demo.app.restapp.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.service.UserService;
import ru.demo.app.restapp.web.controller.UsersApi;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;

import java.net.URI;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController implements UsersApi {
    private final UserService userService;

    @Override
    public ResponseEntity<Void> createUser(UserRequest userRequest) {
        User user = userService.createUser(userRequest);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<UserFullResponse>> findAll(Integer age, String phone, String name, String email, Integer page, Integer size) {
        List<UserFullResponse> all = userService.findAll(age, phone, name, email, page, size);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserFullResponse> findById(Long id) {
        UserFullResponse user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserFullResponse> updateUserEmail(ChangeEmailRequest changeEmailRequest) {
        UserFullResponse response = userService.updateUserEmail(changeEmailRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
