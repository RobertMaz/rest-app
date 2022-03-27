package ru.demo.app.restapp.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.demo.app.restapp.service.UserService;
import ru.demo.app.restapp.web.controller.UsersApiDelegate;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.ErrorResponse;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;
import ru.demo.app.restapp.web.dto.ValidationErrorResponse;

@Slf4j
@RequiredArgsConstructor
public class UserController implements UsersApiDelegate {

  private final UserService userService;

  public ResponseEntity<List<UserFullResponse>> getUsers(Optional<Integer> age, Optional<String> phone,
      Optional<String> name, Optional<String> email, Optional<Integer> page, Optional<Integer> size) {
    log.info("FindAll users by age-{}, phone-{}, name-{}, email-{}, page-{}, size-{}", age, phone, name, email, page,
        size);
    List<UserFullResponse> foundUsers = userService.findAll(age, phone, name, email, page, size);
    log.debug("Found users: {}", foundUsers);
    return new ResponseEntity<>(foundUsers, HttpStatus.OK);
  }

  public ResponseEntity<UserFullResponse> getUserById(@PathVariable Long id) {
    log.info("Find user by id-{}", id);
    return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
  }

  @Operation(summary = "Create new user", responses = {
      @ApiResponse(responseCode = "201", description = "New user is created"),
      @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))})
  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createUser(@Valid @RequestBody UserRequest request) {
    log.info("Creating new user-{}", request);
    final Long id = userService.create(request);
    log.debug("Created user by id-{}", id);
    final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    return ResponseEntity.created(uri).build();
  }

  @Operation(summary = "Update existing user", responses = {
      @ApiResponse(responseCode = "200", description = "User for requested ID is updated", content = @Content(schema = @Schema(implementation = UserFullResponse.class))),
      @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Requested data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping
  public UserFullResponse updateUserEmail(Principal principal, @Valid @RequestBody ChangeEmailRequest request) {
    log.info("Update user-{}", request);
    return userService.updateUserEmail(principal.getName(), request);
  }

  @Operation(summary = "Remove user for ID", responses = @ApiResponse(responseCode = "204", description = "User for requested ID is removed"))
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    log.info("Delete user by id-{}", id);
    userService.delete(id);
  }
}
