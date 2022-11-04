package com.infrasave.controller;

import com.infrasave.bean.AppResponse;
import com.infrasave.bean.LoginRequest;
import com.infrasave.bean.RegisterRequest;
import com.infrasave.exception.UserAlreadyRegisteredException;
import com.infrasave.service.AccountService;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

/**
 * @author huseyinaydin
 */
@RestController
public class AccountController {

  private final AuthenticationProvider authenticationProvider;

  private final AccountService accountService;

  public AccountController(AuthenticationProvider authenticationProvider, AccountService accountService) {
    this.authenticationProvider = authenticationProvider;
    this.accountService = accountService;
  }

  @PostMapping("/register")
  public ResponseEntity register(@Validated @RequestBody RegisterRequest request)
      throws UserAlreadyRegisteredException {
    accountService.register(request);
    return ResponseEntity.ok(AppResponse.successful());
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Validated LoginRequest request) {
    var token = new UsernamePasswordAuthenticationToken(request.email(), request.password());
    Authentication authentication = authenticationProvider.authenticate(token);
    boolean authenticated = authentication.isAuthenticated();
    if (!authenticated) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/exit")
  public ResponseEntity logout(HttpServletRequest request) throws ServletException {
    request.logout();
    return ResponseEntity.ok().build();
  }
}
