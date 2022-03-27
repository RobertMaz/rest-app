package ru.demo.app.restapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class RestAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestAppApplication.class, args);
  }

}
