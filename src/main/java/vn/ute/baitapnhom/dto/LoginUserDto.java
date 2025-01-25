package vn.ute.baitapnhom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    private String email;
    private String password;

    public String getEmail() {return this.email;
    }

    public Object getPassword() {return this.password;
    }
}
