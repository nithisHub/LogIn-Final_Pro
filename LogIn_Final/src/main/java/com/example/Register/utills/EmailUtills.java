package com.example.Register.utills;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtills {
  
  @Autowired
  private JavaMailSender javaMailSender;

  public void sendOtpEmail(String email, String otp) throws MessagingException {
       
    MimeMessage mimeMessage =javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
   
    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject("Verify OTP"); 
    mimeMessageHelper.setText("""
        <div>
        <b><h2>Dear Valueable Customer !</h2></b>
        <h3>You got OTP.. <br>Click this link to verify your accound</h3>
        </div>
    <div>
      <a href="http://localhost:8081/verify_Accound?email=%s&otp=%s" target="_blank">click link to verify</a>
    </div>
    """.formatted(email, otp), true);
    
    javaMailSender.send(mimeMessage);
  }
  
  public void sendSetPassword(String email) throws MessagingException {
    
    MimeMessage mimeMessage =javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
   
    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject("Re-Create password"); 
    mimeMessageHelper.setText("""
        <div>
        <b><h2>Dear Valueable Customer !</h2></b>
        <h3> <br>Click this link to create new Password</h3>
        </div>
    <div>
      <a href="http://localhost:8081/set-password?email=%s" target="_blank">click link to verify</a>
    </div>
    """.formatted(email), true);
    
    javaMailSender.send(mimeMessage);
  }
}
