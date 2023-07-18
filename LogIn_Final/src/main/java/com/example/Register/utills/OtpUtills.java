package com.example.Register.utills;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class OtpUtills {
  public String generateOtp() {
    Random random = new Random();
    int randumnumber = random.nextInt(999999);
    String output = Integer.toString(randumnumber);
    
    while(output.length()<6) {
      output="0"+output;
    }
    return output;
  }

}
