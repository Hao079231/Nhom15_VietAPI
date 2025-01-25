package vn.ute.baitapnhom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
  private String email;
  private String password;
  private String username;

  public String getUsername() {
    return this.username;
  }

  public String getEmail() {
    return this.email;
  }

  public CharSequence getPassword() {
    return this.password;
  }
}
