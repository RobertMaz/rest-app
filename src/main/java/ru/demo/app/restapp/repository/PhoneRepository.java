package ru.demo.app.restapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.demo.app.restapp.domain.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

}