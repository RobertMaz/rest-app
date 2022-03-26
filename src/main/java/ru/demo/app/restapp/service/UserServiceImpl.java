package ru.demo.app.restapp.service;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.domain.Profile;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.model.PhoneRequest;
import ru.demo.app.restapp.model.UserFullResponse;
import ru.demo.app.restapp.model.UserRequest;
import ru.demo.app.restapp.repository.UserRepository;
import ru.demo.app.restapp.repository.specification.Specifications;
import ru.demo.app.restapp.util.Utility;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PhoneService phoneService;
  private final ProfileService profileService;

  /**
   * Получение информации о человеке. Если не найдено, отдавать 404:NotFound
   */
  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public UserFullResponse getById(Long id) {
    Optional<User> userOpt = userRepository.findById(id);
    User user = userOpt.orElseThrow(
        () -> new EntityNotFoundException(Utility.getMessage("User by id {1} not found.", id)));
    log.info("User found: {}", user);
    return UserFullResponse.from(user);
  }

  /**
   * Создание новой записи о человеке
   */
  @Override
  @Transactional
  public Long create(@Nonnull UserRequest request) {
    User user = updateUser(request, new User());
    List<PhoneRequest> phonesReq = request.getPhones();
    List<Phone> phones = phoneService.saveAll(phonesReq, user);
    Profile profile = profileService.save(request.getProfile(), user);
    user.setPhones(phones);
    user.setProfile(profile);
    log.info("User created: {}", user);
    user = userRepository.save(user);
    return user.getId();
  }


  private User updateUser(UserRequest request, User oldUser) {
    if (oldUser.getId() != null) {
      if (!Objects.equals(request.getAge(), oldUser.getAge())) {
        throw new IllegalArgumentException("Age mustn't change during update");
      }
      if (!Objects.equals(request.getName(), oldUser.getName())) {
        throw new IllegalArgumentException("Name mustn't change during update");
      }
    }
    oldUser.setName(request.getName()).setAge(request.getAge()).setEmail(request.getEmail());
    return userRepository.save(oldUser);
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
        () -> new EntityNotFoundException(Utility.getMessage("User by id {1} not found", id)));
    user = updateUser(request, user);
    user = userRepository.save(user);
    log.info("User updated: {}", user);
    return UserFullResponse.from(user);
  }

  /**
   * Удаление информации о человеке и удаление его из отдела. Если не найдено, ничего не делать
   */
  @Override
  @Transactional
  public void delete(Long id) {
    userRepository.deleteById(id);
    log.info("User deleted by id: {}", id);
  }

  @Override
  public List<UserFullResponse> findAll(Optional<Integer> age, Optional<String> phone,
      Optional<String> name, Optional<String> email, Optional<Integer> page,
      Optional<Integer> size) {
    Specification<User> spec = Specification.where(null);
    if (age.isPresent() && age.get() > -1) {
      log.info("Adding {} age to filter", age.get());
      spec = spec.and(Specifications.equalField("age", age.get()));
    }
    if (name.isPresent() && !name.get().isBlank()) {
      log.info("Adding {} name to filter", name.get());
      spec = spec.and(Specifications.fieldLike("name", name.get()));
    }
    if (email.isPresent() && !email.get().isBlank()) {
      log.info("Adding {} email to filter", email.get());
      spec = spec.and(Specifications.equalField("email", email.get()));
    }
    if (phone.isPresent() && !phone.get().isBlank()) {
      spec = spec.and(Specifications.whereChildFieldListContains("phones", "value", phone.get()));
    }
    PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, size.orElse(DEFAULT_PAGE_SIZE));
    return userRepository
        .findAll(spec, pageRequest)
        .stream()
        .map(UserFullResponse::from)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<User> findByName(String userName) {
    return userRepository.findByName(userName);
  }
}
