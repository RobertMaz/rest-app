package ru.demo.app.restapp.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {

  private Specifications() {

  }

  public static <T> Specification<T> fieldLike(String field, String value) {
    return (root, query, builder) -> builder.like(root.get(field), "%" + value + "%");
  }

  public static <T> Specification<T> equalField(String field, Object value) {
    return (root, query, builder) -> builder.equal(root.get(field), value);
  }

  public static <T> Specification<T> whereChildFieldListContains(String childTableName,
      String fieldName, String value) {
    return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.equal(
        root.join(childTableName).get(fieldName), value);
  }
}
