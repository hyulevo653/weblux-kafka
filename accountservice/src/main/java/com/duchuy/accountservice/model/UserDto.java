package com.duchuy.accountservice.model;

import com.duchuy.accountservice.data.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
  private String id;
  private String username;
  private String password;

  public static User dtoToEntity() {
    return null;
  }

  public static UserDto entityToModel(User user) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .build();
  }
}
