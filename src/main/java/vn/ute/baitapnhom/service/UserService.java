package vn.ute.baitapnhom.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import vn.ute.baitapnhom.model.User;
import vn.ute.baitapnhom.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository, EmailService emailService){
    this.userRepository = userRepository;
  }

  public List<User> allUser(){
    List<User> users = new ArrayList<>();
    userRepository.findAll().forEach(users::add);
    return users;
  }
}
