package ru.demo.app.restapp.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.demo.app.restapp.service.AuthService;
import ru.demo.app.restapp.web.controller.AuthApi;
import ru.demo.app.restapp.web.dto.AccessDto;
import ru.demo.app.restapp.web.dto.AuthenticationRequestDto;
import ru.demo.app.restapp.web.dto.UserRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    public ResponseEntity<AccessDto> auth(AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.ok(authService.auth(authenticationRequestDto));
    }

    @Override
    public ResponseEntity<Void> registerNewUser(UserRequest userRequest) {
        authService.registerNewUser(userRequest);
        return null;
    }
}
