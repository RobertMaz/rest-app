package ru.demo.app.restapp.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.model.PhoneRequest;
import ru.demo.app.restapp.model.ProfileRequest;
import ru.demo.app.restapp.model.UserFullResponse;
import ru.demo.app.restapp.model.UserRequest;
import ru.demo.app.restapp.model.UserShortResponse;
import ru.demo.app.restapp.repository.PhoneRepository;
import ru.demo.app.restapp.repository.ProfileRepository;
import ru.demo.app.restapp.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final PhoneRepository phoneRepository;


  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<UserShortResponse> findAll() {
    return userRepository.findAll().stream().map(UserShortResponse::from)
                         .collect(Collectors.toList());
  }

  /**
   * Получение информации о человеке. Если не найдено, отдавать 404:NotFound
   */
  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public UserFullResponse getById(Long id) {
    Optional<User> user = userRepository.findById(id);
    return UserFullResponse.from(
        user.orElseThrow(() -> new EntityNotFoundException("User not found by id-" + id)));
  }

  /**
   * Создание новой записи о человеке
   */
  @Override
  @Transactional
  public Long create(@Nonnull UserRequest request) {
    User user = new User();
    user = updateUser(request, user);
    List<Profile> profile = user.getProfile();

    userRepository.save(user);

    profileRepository.saveAll(profile);
    phoneRepository.saveAll(user.getPhones());

    return user.getId();
  }

  private User updateUser(UserRequest request, User oldUser) {
    User user = new User();
    List<PhoneRequest> phoneRequests = request.getPhones();
    List<Phone> phones = null;
    if (phoneRequests != null) {
      phones = phoneRequests
          .stream()
          .filter(Objects::nonNull)
          .map(createPhoneRequest(user))
          .collect(Collectors.toList());
    }

    ProfileRequest profileRequest = request.getProfile();
    Profile profile = null;
    if (profileRequest != null) {
      Long oldProfileId = null;
      if (oldUser.getProfile() != null && !oldUser.getProfile().isEmpty()) {
        oldProfileId = oldUser.getProfile().get(0).getId();
      }
      profile = new Profile()
          .setCash(profileRequest.getCash())
          .setUser(user)
          .setId(oldProfileId);
    }

    user.setId(oldUser.getId());
    user.setName(request.getName())
        .setAge(request.getAge())
        .setEmail(request.getEmail());
    user.setProfile(Arrays.asList(profile));
    user.setPhones(phones);
    log.info(user.toString());
    return user;
  }

  private Function<PhoneRequest, Phone> createPhoneRequest(User finalUser) {
    return phoneRequest -> new Phone().setUser(finalUser).setValue(phoneRequest.getValue());
  }

  /**
   * Обновление информации о человеке. Если не найдено, отдавать 404:NotFound
   */
  @Nonnull
  @Override
  @Transactional
  public UserFullResponse update(Long id, @Nonnull UserRequest request) {
    Optional<User> userOptional = userRepository.findById(id);
    User user = userOptional.orElseThrow(
        () -> new EntityNotFoundException("User not found by id-" + id));
    user = updateUser(request, user);
    return UserFullResponse.from(userRepository.save(user));
  }

  /**
   * Удаление информации о человеке и удаление его из отдела. Если не найдено, ничего не делать
   */
  @Override
  @Transactional
  public void delete(Long id) {
    userRepository.deleteById(id);
  }
}
