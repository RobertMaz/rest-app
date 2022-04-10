package ru.demo.app.restapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.demo.app.restapp.util.UsersUtil.buildUser;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.repository.UserRepository;
import ru.demo.app.restapp.util.UsersUtil;
import ru.demo.app.restapp.util.mapper.PhoneMapper;
import ru.demo.app.restapp.util.mapper.UserMapper;
import ru.demo.app.restapp.web.dto.ChangeEmailRequest;
import ru.demo.app.restapp.web.dto.UserFullResponse;
import ru.demo.app.restapp.web.dto.UserRequest;

@SpringBootTest
class UserServiceImplTest {

  private static final int COUNT = 2;

  private UserService userService;
  @MockBean
  private UserRepository userRepository;
  @MockBean
  private PhoneService phoneService;
  @MockBean
  private ProfileService profileService;
  @MockBean
  private UserMapper userMapper;
  @MockBean
  private PhoneMapper phoneMapper;
  private final PasswordEncoder encoder = new BCryptPasswordEncoder();

  private static final Random random = new Random();

  @BeforeEach
  void init() {
    this.userService = new UserServiceImpl(userRepository, phoneService, profileService, encoder);
  }

  @Test
  void findById() {
    Long anyId = anyLong();
    when(userRepository.findById(anyId)).thenReturn(Optional.of(buildUser(anyId)));

    ResponseEntity<UserFullResponse> user = userService.findById(anyId);
    assertNotNull(user.getBody());
    assertEquals(user.getBody().getId(), anyId);
  }

  @Test
  void findByIdNegative() {
    assertThrows(RuntimeException.class, () -> userService.findById(0L));
  }


  @Test
  void createUser() {
    Long id = (long) random.nextInt(100);
    User userReq = new User();
    userReq.setId(id);
    userReq.setEmail("email");
    userReq.setName("name");

    doReturn(userReq).when(userRepository).save(any(User.class));

    UserRequest request = new UserRequest();
    request.setEmail("email");
    request.setName("name");
    request.setPassword("pwd");

    ResponseEntity<Void> created = userService.createUser(request);
    //
    verify(phoneService, times(1)).saveAll(any(), eq(userReq));
    verify(profileService, times(1)).save(any(), eq(userReq));
    verify(userRepository, times(1)).save(userReq);
    //
    URI location = created.getHeaders().getLocation();
    assertNotNull(location);
    String url = location.getScheme() + "://" + location.getHost() + "/" + id;
    assertEquals(url, location.toString());
  }

  @Test
  void updateUserEmail() {
    String username = "user";
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, "password"));
    Long anyId = (long) random.nextInt(100);
    String email = "emailNew@email.com";
    User user = new User().setEmail("oldEmail@email.com").setName(username).setId(anyId);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    doReturn(user.setEmail(email)).when(userRepository).save(user);

    ChangeEmailRequest request = new ChangeEmailRequest();
    request.setEmail(email);

    ResponseEntity<UserFullResponse> updated = userService.updateUserEmail(request);
    UserFullResponse userResponse = updated.getBody();
    //
    assertNotNull(userResponse);
    assertEquals(email, userResponse.getEmail());
    assertEquals(anyId, userResponse.getId());
    assertEquals(user.getName(), userResponse.getName());
    verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
  }

  @Test
  void updateEmailNegative() {
    ChangeEmailRequest emailRequest = new ChangeEmailRequest();
    emailRequest.setEmail("empty");
    assertThrows(RuntimeException.class, () -> userService.updateUserEmail(emailRequest));
    verify(userRepository, times(0)).save(ArgumentMatchers.any(User.class));
  }

  @Test
  void deleteUser() {
    Long anyId = (long) random.nextInt(100);
    userService.deleteUser(anyId);
    verify(userRepository, times(1)).deleteById(anyId);
    assertThrows(RuntimeException.class, () -> userService.findById(0L));
  }

  @Test
  void findAll() {
    List<User> users = LongStream.range(0, COUNT).mapToObj(UsersUtil::buildUser).collect(Collectors.toList());

    Page<User> usersPage = new PageImpl<>(users);

    when(userRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(usersPage);

    ResponseEntity<List<UserFullResponse>> foundPeople = userService.findAll(null, null, null, null, null, null);
    assertNotNull(foundPeople.getBody());
    assertEquals(COUNT, foundPeople.getBody().size());

    Set<Long> allPeopleIds = users.stream().map(User::getId).collect(Collectors.toSet());
    Set<Long> foundIds = foundPeople.getBody().stream().map(UserFullResponse::getId).collect(Collectors.toSet());
    allPeopleIds.removeAll(foundIds);
    assertEquals(0, allPeopleIds.size());
  }

  @Test
  void findByName() {
    String name = anyString();
    when(userRepository.findByName(name)).thenReturn(Optional.of(buildUser(name)));

    Optional<User> user = userService.findByName(name);
    assertTrue(user.isPresent());
    assertEquals(name, user.get().getName());
    verify(userRepository, times(1)).findByName(name);
  }

  @Test
  void findByUserName() {
    String name = anyString();
    when(userRepository.findByUsername(name)).thenReturn(Optional.of(buildUser(name)));

    Optional<User> user = userService.findByUserName(name);
    assertTrue(user.isPresent());
    assertEquals(name, user.get().getName());
    verify(userRepository, times(1)).findByUsername(name);
  }

  @Test
  void loadUserByUsername() {
    String name = "anyName";
    when(userRepository.findByUsername(name)).thenReturn(Optional.of(buildUser(name)));

    if (userService instanceof UserDetailsService) {
      UserDetails userDetails = ((UserDetailsService) userService).loadUserByUsername(name);
      assertNotNull(userDetails);
      assertEquals(name, userDetails.getUsername());
      verify(userRepository, times(1)).findByUsername(name);
    }
  }
}