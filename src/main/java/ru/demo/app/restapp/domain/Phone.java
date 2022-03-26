package ru.demo.app.restapp.domain;

import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name = "PHONES")
@Accessors(chain = true)
@RequiredArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Phone {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "VALUE")
  private String value;

  @ManyToOne
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
    Phone profile = (Phone) o;
    return id != null && Objects.equals(id, profile.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}