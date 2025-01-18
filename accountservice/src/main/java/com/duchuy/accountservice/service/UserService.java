package com.duchuy.accountservice.service;

import com.duchuy.accountservice.authen.jwt.JwtTokenProvider;
import com.duchuy.accountservice.model.LoginDTO;
import com.duchuy.accountservice.model.LoginResponse;
import com.duchuy.accountservice.model.UserDto;
import com.duchuy.accountservice.repository.UserRepository;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

  @Autowired
  UserRepository repository;

  @Autowired
  private JwtTokenProvider tokenProvider;

  public Mono<LoginResponse> login(LoginDTO loginDTO) {
    log.info("Login service called");
    return getByUsername(loginDTO.getUsername())
        .switchIfEmpty(Mono.just(UserDto.builder().build())).flatMap(
            userDto -> {
              LoginResponse response = new LoginResponse();
              if (userDto.getId() == null) {
                response.setToken("");
                response.setError(true);
                response.setError_description("Nguoi dung khong ton tai");
              } else {
                if (userDto.getPassword().equals(loginDTO.getPassword())) {
                  response.setToken(
                      tokenProvider.generateToken(userDto.getId(), userDto.getUsername()));
                  response.setError(false);
                  response.setError_description("Dang nhap thanh cong");
                } else {
                  response.setToken("");
                  response.setError(true);
                  response.setError_description("Mat khau sai");
                }
              }
              return Mono.just(response);
            }
        );
  }

  public Mono<UserDto> getByUsername(String username) {
    return repository.findFirstByUsername(username).map(UserDto::entityToModel);
  }

  public Mono<UserDto> getById(String id) {
    return repository.findById(id).map(UserDto::entityToModel);
  }

  public Mono<String> validateToken(String token) {
    if (StringUtils.isBlank(token)) {
      return Mono.error(new Exception("Invalid token"));
    }
    String id = tokenProvider.getIdFromJWT(token);
    //validate thời gian hết hạn
    //....
    return getById(id).switchIfEmpty(Mono.error(new Exception("Invalid token")))
        .map(UserDto::getId);
  }
}
