package vn.ute.baitapnhom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {
    private String email;
    private String verificationCode;

    public String getEmail() {return this.email;
    }

    public String getVerificationCode() {return this.verificationCode;
    }
}
