package vn.ute.baitapnhom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.ute.baitapnhom.dto.LoginUserDto;
import vn.ute.baitapnhom.dto.RegisterUserDto;
import vn.ute.baitapnhom.dto.VerifyUserDto;
import vn.ute.baitapnhom.model.User;
import vn.ute.baitapnhom.responses.LoginResponse;
import vn.ute.baitapnhom.service.AuthenticationService;
import vn.ute.baitapnhom.service.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
  private final JwtService jwtService;
  private final AuthenticationService authenticationService;

  public AuthenticationController(JwtService jwtService,
      AuthenticationService authenticationService) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/signup")
  public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto){
    User registeredUser = authenticationService.signup(registerUserDto);
    return ResponseEntity.ok(registeredUser);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
    User authenticateUser = authenticationService.authenticate(loginUserDto);
    String jwtToken = jwtService.generateToken(authenticateUser);
    LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpiration());
    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/verify")
  public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto){
    try{
      authenticationService.verifyUser(verifyUserDto);
      return ResponseEntity.ok("Account verified successfully");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/resend")
  public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
    try{
      authenticationService.resendVerificationCode(email);
      return ResponseEntity.ok("Verification code sent");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
