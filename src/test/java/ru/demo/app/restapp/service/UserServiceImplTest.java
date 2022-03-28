package ru.demo.app.restapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.demo.app.restapp.util.UsersUtil.buildUser;

import java.net.URI;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.repository.UserRepository;
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
  private PasswordEncoder encoder = new BCryptPasswordEncoder();

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
    assertEquals(user.getBody().getId(), anyId);
  }

  @Test
  void createUser() {
    User userReq = new User();
    Long id = 1L;
    userReq.setId(id);
    userReq.setName("name");
    userReq.setEmail("email");
    userReq.setPassword(encoder.encode("pwd"));

    doReturn(userReq).when(userRepository).save(any(User.class));

    UserRequest request = new UserRequest();
    request.setEmail("email");
    request.setName("name");
    request.setPassword("pwd");
    ResponseEntity<Void> created = userService.createUser(request);
    verify(phoneService, times(1)).saveAll(any(), eq(userReq));
    verify(profileService, times(1)).save(any(), eq(userReq));
    URI location = created.getHeaders().getLocation();
    assertNotNull(location);
    String url = location.getScheme() + "://" + location.getHost() + "/" + id;
    assertEquals(url, location.toString());
  }

  @Test
  void updateUserEmail() {
    assertTrue(true);
  }

  @Test
  void deleteUser() {
    assertTrue(true);
  }

  @Test
  void findAll() {
    assertTrue(true);
  }

  @Test
  void findByName() {
    assertTrue(true);
  }

  @Test
  void findByUserName() {
    assertTrue(true);
  }

  @Test
  void loadUserByUsername() {
    assertTrue(true);
  }
}