package ru.demo.app.restapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.Role;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.repository.UserRepository;
import ru.demo.app.restapp.repository.specification.Specifications;
import ru.demo.app.restapp.util.Utility;
import ru.demo.app.restapp.util.mapper.UserMapper;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.PhoneDto;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

  private final UserRepository userRepository;
  private final PhoneService phoneService;
  private final ProfileService profileService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Получение информации о человеке. Если не найдено, отдавать 404:NotFound
   */
  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<UserFullResponse> findById(Long id) {
    Optional<User> userOpt = userRepository.findById(id);
    log.info("Find user by id-{}", id);
    UserFullResponse user = userOpt
        .map(USER_MAPPER::userToFullUserResponse)
        .orElseThrow(() -> new EntityNotFoundException(Utility.getMessage("User by id {1} not found.", id)));
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Создание новой записи о человеке
   */
  @Override
  @Transactional
  public ResponseEntity<Void> createUser(@Nonnull UserRequest request) {
    log.info("Creating new user by username-{}", request.getUsername());
    User user = new User()
        .setName(request.getName())
        .setAge(request.getAge())
        .setEmail(request.getEmail())
        .setPassword(passwordEncoder.encode(request.getPassword()))
        .setUsername(request.getUsername())
        .setRoles(List.of(new Role().setName("user")));
    user = userRepository.save(user);
    List<PhoneDto> phonesReq = request.getPhones();
    List<Phone> phones = phoneService.saveAll(phonesReq, user);
    Profile profile = profileService.save(request.getProfile(), user);
    user.setPhones(phones);
    user.setProfile(profile);
    user = userRepository.save(user);
    log.debug("User created: {}", user.getUsername());
    final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }


  /**
   * Update user email.
   *
   * @param request - request with email
   */
  @Nonnull
  @Override
  @Transactional
  public ResponseEntity<UserFullResponse> updateUserEmail(@Nonnull ChangeEmailRequest request) {
    log.info("Update user-{}", request);
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username;
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }
    Optional<User> userOptional = userRepository.findByUsername(username);
    String finalUsername = username;
    User user = userOptional.orElseThrow(
        () -> new EntityNotFoundException(Utility.getMessage("User '{}' not found", finalUsername)));
    Optional<User> userWithEmail = userRepository.findByEmail(request.getEmail());
    if (userWithEmail.isPresent()) {
      throw new IllegalArgumentException("Email already used by another user. Please enter correct password");
    }
    user.setEmail(request.getEmail());
    user = userRepository.save(user);
    log.debug("User updated: {}", user);
    UserFullResponse response = USER_MAPPER.userToFullUserResponse(user);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Удаление информации о человеке и удаление его из отдела. Если не найдено, ничего не делать
   */
  @Override
  @Transactional
  public ResponseEntity<Void> deleteUser(Long id) {
    log.info("Delete user by id-{}", id);

    userRepository.deleteById(id);
    log.debug("User deleted by id: {}", id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<List<UserFullResponse>> findAll(Integer age, String phone, String name, String email,
      Integer page, Integer size) {
    log.info("FindAll users by age-{}, phone-{}, name-{}, email-{}, page-{}, size-{}", age, phone, name, email, page,
             size);
    Specification<User> spec = Specification.where(null);
    if (age != null) {
      log.debug("Adding {} age to filter", age);
      spec = spec.and(Specifications.equalField("age", age));
    }
    if (name != null) {
      log.debug("Adding {} name to filter", name);
      spec = spec.and(Specifications.fieldLike("name", name));
    }
    if (email != null) {
      log.debug("Adding {} email to filter", email);
      spec = spec.and(Specifications.equalField("email", email));
    }
    if (phone != null) {
      log.debug("Adding {} phone to filter", phone);
      spec = spec.and(Specifications.whereChildFieldListContains("phones", "value", phone));
    }
    PageRequest pageRequest = PageRequest.of(page == null ? 0 : page - 1, size == null ? DEFAULT_PAGE_SIZE : size);
    List<UserFullResponse> foundUsers = userRepository
        .findAll(spec, pageRequest)
        .stream()
        .map(USER_MAPPER::userToFullUserResponse)
        .collect(Collectors.toList());
    log.debug("Found users: {}", foundUsers);
    return new ResponseEntity<>(foundUsers, HttpStatus.OK);
  }

  public Optional<User> findByName(String userName) {
    return userRepository.findByName(userName);
  }

  @Override
  public Optional<User> findByUserName(String username) {
    log.info("Find user by username: {}", username);
    return userRepository.findByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isEmpty()) {
      log.error("User with username '{}' not found", username);
      throw new UsernameNotFoundException("User with username: " + username + " not found");
    }
    log.info("loadUserByUsername - user with username: {} successfully loaded", username);
    User user = userOpt.get();
    List<SimpleGrantedAuthority> authorities = user
        .getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
  }
}
