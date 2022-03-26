package ru.demo.app.restapp.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@ToString
@Cacheable
@Table(name = "PROFILES")
@Accessors(chain = true)
@RequiredArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profile {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "CASH")
  private BigDecimal cash;

  @Column(name = "INITIAL_VALUE", updatable = false)
  private BigDecimal initialValue;

  @OneToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Profile profile = (Profile) o;
    return id != null && Objects.equals(id, profile.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}