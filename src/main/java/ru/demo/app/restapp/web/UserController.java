package ru.demo.app.restapp.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.demo.app.restapp.model.ErrorResponse;
import ru.demo.app.restapp.model.UserFullResponse;
import ru.demo.app.restapp.model.UserRequest;
import ru.demo.app.restapp.model.ValidationErrorResponse;
import ru.demo.app.restapp.service.UserService;

@Tag(name = "User REST API operations")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "Returns a list of users filtered based on the query parameters", responses = {
      @ApiResponse(responseCode = "200", description = "Users", content = @Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserFullResponse.class))))})
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public List<UserFullResponse> findAll(
      @RequestParam(required = false, name = "age") Optional<Integer> age,
      @RequestParam(required = false, name = "phone") Optional<String> phone,
      @RequestParam(required = false, name = "name") Optional<String> name,
      @RequestParam(required = false, name = "email") Optional<String> email,
      @RequestParam(required = false, name = "page") Optional<Integer> page,
      @RequestParam(required = false, name = "size") Optional<Integer> size) {
    return userService.findAll(age, phone, name, email, page, size);
  }

  @Operation(summary = "Get user by ID", responses = {
      @ApiResponse(responseCode = "200", description = "User for requested ID", content = @Content(schema = @Schema(implementation = UserFullResponse.class))),
      @ApiResponse(responseCode = "404", description = "Requested data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
  public UserFullResponse user(@PathVariable Long id) {
    return userService.getById(id);
  }

  @Operation(summary = "Create new user", responses = {
      @ApiResponse(responseCode = "201", description = "New user is created"),
      @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))})
  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createUser(@Valid @RequestBody UserRequest request) {
    final Long id = userService.create(request);
    final URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(id)
        .toUri();

    return ResponseEntity.created(uri).build();
  }

  @Operation(summary = "Update existing user", responses = {
      @ApiResponse(responseCode = "200", description = "User for requested ID is updated", content = @Content(schema = @Schema(implementation = UserFullResponse.class))),
      @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Requested data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public UserFullResponse updateUser(@PathVariable Long id,
      @Valid @RequestBody UserRequest request) {
    return userService.update(id, request);
  }

  @Operation(summary = "Remove user for ID", responses = @ApiResponse(responseCode = "204", description = "User for requested ID is removed"))
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.delete(id);
  }
}
