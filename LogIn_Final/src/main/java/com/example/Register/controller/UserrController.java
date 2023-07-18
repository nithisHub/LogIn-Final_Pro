package com.example.Register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.Register.request.LoginRequest;
import com.example.Register.request.UserrRequest;
import com.example.Register.service.UserrService;

@RestController
public class UserrController {

  @Autowired
  private UserrService userrService;

  @PostMapping("/register_Accound")
  public ResponseEntity<String> register(@RequestBody UserrRequest userrRequest) {
    return new ResponseEntity<>(userrService.registerService(userrRequest), HttpStatus.OK);
  }

  @PutMapping("/verify_Accound")
  public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String otp) {
    return new ResponseEntity<>(userrService.verifyOtp(email, otp), HttpStatus.OK);
  }

  @PostMapping("/regenerateOtp")
  public ResponseEntity<String> reSendOtp(@RequestBody UserrRequest userrRequest) {
    return new ResponseEntity<>(userrService.reGenerateOtp(userrRequest.getEmail()), HttpStatus.OK);

  }

  @PostMapping("/login")
  public ResponseEntity<String> logInPage(@RequestBody LoginRequest loginRequest) {
    return new ResponseEntity<>(
        userrService.logIn(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
  }

  @PostMapping("/forgotpassword")
  public ResponseEntity<String> forgotPass(@RequestBody LoginRequest loginRequest) {
    return new ResponseEntity<>(
        userrService.forgotPassword(loginRequest.getEmail()), HttpStatus.OK);
  }
  @PutMapping("set-password")
  public ResponseEntity<String> setPassword(@RequestParam String email,@RequestHeader String newPassword) {
    return new ResponseEntity<>(userrService.setpassword(email,newPassword),HttpStatus.OK);
    
  }
}
