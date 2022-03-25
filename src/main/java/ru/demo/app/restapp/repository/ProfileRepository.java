package ru.demo.app.restapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.demo.app.restapp.domain.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

}