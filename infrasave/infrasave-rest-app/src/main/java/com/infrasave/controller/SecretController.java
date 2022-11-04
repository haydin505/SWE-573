package com.infrasave.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("secret")
public class SecretController {

  @Secured({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping
  public String secret() {
    return "Secret";
  }
}
