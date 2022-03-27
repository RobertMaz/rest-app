package ru.demo.app.restapp.service;

import static ru.demo.app.restapp.util.Utility.isEmpty;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.demo.app.restapp.domain.Phone;
import ru.demo.app.restapp.domain.User;
import ru.demo.app.restapp.repository.PhoneRepository;
import ru.demo.app.restapp.web.dto.PhoneDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

  private final PhoneRepository phoneRepository;

  @Override
  @Nonnull
  @Transactional
  public List<Phone> saveAll(@Nullable List<PhoneDto> phoneDtos, User user) {
    Map<String, Optional<Phone>> phonesMap = getUserPhonesMap(user);
    Set<String> requestNumbers = new HashSet<>();
    List<Phone> savedPhones = new LinkedList<>();

    if (!isEmpty(phoneDtos)) {
      for (PhoneDto phone : phoneDtos) {
        requestNumbers.add(phone.getValue());
        Phone newPhone;
        if (!phonesMap.containsKey(phone.getValue())) {
          newPhone = new Phone();
          newPhone.setUser(user);
          newPhone.setValue(phone.getValue());
          phoneRepository.save(newPhone);
        } else {
          newPhone = phonesMap.get(phone.getValue()).orElseThrow();
        }
        savedPhones.add(newPhone);
      }
    }
    phonesMap
        .keySet()
        .stream()
        .filter(number -> !requestNumbers.contains(number))
        .map(number -> phonesMap.get(number).orElseThrow())
        .forEach(phoneRepository::delete);
    log.info("Saved phones {}", phonesMap);
    return savedPhones;
  }

  @Nonnull
  private Map<String, Optional<Phone>> getUserPhonesMap(@Nonnull User user) {
    return user.getPhones() != null ? user
        .getPhones()
        .stream()
        .collect(Collectors.groupingBy(Phone::getValue, Collectors.<Phone>reducing((a, b) -> a)))
        : Map.of();
  }
}
