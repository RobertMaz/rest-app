package ru.demo.app.restapp.model;

import lombok.Data;

@Data
public class AuthenticationRequestDto {

  private String username;
  private String password;
}
