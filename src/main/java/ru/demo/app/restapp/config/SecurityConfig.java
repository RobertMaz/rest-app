package ru.demo.app.restapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ru.demo.app.restapp.security.jwt.JwtConfigurer;
import ru.demo.app.restapp.security.jwt.JwtTokenProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    //@formatter:off
    httpSecurity
        .httpBasic().disable()
          .csrf().disable()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .authorizeRequests()
          .antMatchers(LOGIN_ENDPOINT).permitAll()
          .anyRequest().authenticated()
        .and()
          .apply(new JwtConfigurer(jwtTokenProvider));
    //@formatter:off
  }
}
