package com.example.Register.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.Register.entity.Userr;
import com.example.Register.repo.UserrRepository;
import com.example.Register.request.UserrRequest;
import com.example.Register.utills.EmailUtills;
import com.example.Register.utills.OtpUtills;
import jakarta.mail.MessagingException;

@Service
public class UserrService {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private OtpUtills otpUtills;

  @Autowired
  private EmailUtills emailUtills;

  @Autowired
  private UserrRepository userrRepository;

  public String registerService(UserrRequest userrRequest) {
    String otp = otpUtills.generateOtp();
    Userr u = userrRepository.findByEmail(userrRequest.getEmail());
    if (u == null) {
      try {
        emailUtills.sendOtpEmail(userrRequest.getEmail(), otp);
      } catch (MessagingException e) {
        throw new RuntimeException("unable to send OTP... Try again !");
      }
      Userr userr = new Userr();
      userr.setName(userrRequest.getName());
      userr.setOtp(otp);
      userr.setOtpGeneratedTime(LocalDateTime.now());

      String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
      Pattern pattern = Pattern.compile(emailRegex);
      Matcher matcher = pattern.matcher(userrRequest.getEmail());

      if (matcher.matches()) {
        // Valid EmailId
        userr.setEmail(userrRequest.getEmail());

        if (userrRequest.getPassword() == null) {
          return "Password is null !";
        } else {
          // Password not null
          String passwordRegex = "(?=.*[A-Z])(?=.*\\d).{7,}";
          Pattern paswdPattern = Pattern.compile(passwordRegex);
          Matcher pasedMatcher = paswdPattern.matcher(userrRequest.getPassword());
          if (pasedMatcher.matches()) {
            // valid Password
            BCryptPasswordEncoder bcrypaswd = new BCryptPasswordEncoder();
            String encryPassword = bcrypaswd.encode(userrRequest.getPassword());
            userr.setPassword(encryPassword);
            userrRepository.save(userr);

            return "Check Your Maill and verify Your OTP !";
          } else {
            return "Your Password is Week !";
          }
        }
      } else {
        return "UnValid Email Id ! ";
      }


    } else {

      if (u.getActive() == false) {
        return "E-mail already exist ! But Not Active. Enter Your OTP ! or Re-Generate OTP";
      } else {
        return "Already you Register your Email:  " + u.getEmail()
            + "If you forget your Password, Re-Generate Your New OTP ";
      }
    }
  }

  public String verifyOtp(String email, String otp) {

    Userr userr = userrRepository.findByEmail(email);
    if (userr.getEmail().isEmpty()) {
      return "E-mail NOT Exist !";
    } else if (userr.getOtp().equals(otp)) {
      if (Duration.between(userr.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < 5
          * 60) {
        userr.setActive(true);
        userrRepository.save(userr);
        return "Done ! Your Accound Verified !! You can LogIn";
      } else {
        userrRepository.delete(userr);
        return "Time out.. Re-Register Your Accound !";
      }
    } else {
      return " OTP is NOT match, Try again !!";
    }
  }

  public String reGenerateOtp(String email) {
    Userr userr = userrRepository.findByEmail(email);
    if (userr.getEmail().isEmpty()) {
      return "E-mail NOT Exist !";
    } else {
      String otp = otpUtills.generateOtp();

      try {
        emailUtills.sendOtpEmail(email, otp);
      } catch (MessagingException e) {
        throw new RuntimeException("unable to send OTP... Try again !");
      }

      userr.setOtp(otp);
      userr.setOtpGeneratedTime(LocalDateTime.now());
      userrRepository.save(userr);

      return "Done ! OTP was send. check your email: " + userr.getEmail();
    }
  }

  public String logIn(String email, String password) {

    Userr userr = userrRepository.findByEmail(email);

    if (userr == null) {
      return "You much Register Your Accound First !";
    } else {

      BCryptPasswordEncoder bcrypaswd2 = new BCryptPasswordEncoder();
      Boolean passwordMatch = bcrypaswd2.matches(password, userr.getPassword());
      System.out.println(passwordMatch);

      if (passwordMatch) {
        if (userr.getActive() == true) {
          return "Successfully Login !";
        } else {
          return "Conform Your OTP Verification !";
        }
      } else {
        return "Your Password Not Match. Try again !";
      }
    }

  }

  public String forgotPassword(String email) {
    Userr userr = userrRepository.findByEmail(email);
    if (userr == null) {
      return "Register your email id : " + email;
    } else {

      try {
        emailUtills.sendSetPassword(email);
      } catch (Exception e) {
        throw new RuntimeException("Unable to Reach Your Email. try again to set New password ! ");
      }
      return "Check Your Email to set new Password !";
    }
  }

  public String setpassword(String email, String newPassword) {
    Userr userr = userrRepository.findByEmail(email);
    if (userr == null) {
      return "Can't find that Email ! Its not register before !! ";
    } else {

      String passwordRegex = "(?=.*[A-Z])(?=.*\\d).{7,}";
      Pattern paswdPattern = Pattern.compile(passwordRegex);
      Matcher pasedMatcher = paswdPattern.matcher(newPassword);
      if (pasedMatcher.matches()) {
        BCryptPasswordEncoder bcrypaswd3 = new BCryptPasswordEncoder();
        String encryResetPassword = bcrypaswd3.encode(newPassword);
        userr.setPassword(encryResetPassword);
        userrRepository.save(userr);

        return "New Password set successfully ! LogIn with new Password !!";
      } else {
        return "Your Password is Week !";
      }

    }
  }
}


