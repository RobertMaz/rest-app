package ru.demo.app.restapp.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Cacheable
@Table(name = "USERS")
@Accessors(chain = true)
@RequiredArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "AGE")
  private Integer age;

  @Column(name = "EMAIL")
  private String email;

  @OneToMany(fetch = FetchType.EAGER, targetEntity = Phone.class, mappedBy = "user", cascade = CascadeType.REMOVE)
  private List<Phone> phones;

  @OneToOne(targetEntity = Profile.class, mappedBy = "user", cascade = CascadeType.REMOVE)
  private Profile profile;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + "id = " + id + ", " + "name = " + name + ", "
        + "age = " + age + ", " + "email = " + email + ")";
  }
}
