package com.infrasave.infrasaverestapp;

import com.infrasave.service.EmailService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class InfrasaveRestAppApplicationTests {


  @Autowired
  private EmailService emailService;

  @Test
  void sendMail() {
    emailService.sendSimpleMessage("infrasave.app@gmail.com", "Test", "Test");
  }
}
