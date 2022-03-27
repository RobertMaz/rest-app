package ru.demo.app.restapp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

//@Table
//@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Role {

  //  @Id
//  @Column(name = "ID")
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //  @Column(name = "NAME")
  private String name;

}
