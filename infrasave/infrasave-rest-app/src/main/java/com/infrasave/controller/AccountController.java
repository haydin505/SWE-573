package com.infrasave.controller;

import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.LoginRequest;
import com.infrasave.bean.RegisterRequest;
import com.infrasave.bean.ResetPasswordRequest;
import com.infrasave.bean.ResetPasswordWithTokenRequest;
import com.infrasave.exception.UserAlreadyRegisteredException;
import com.infrasave.service.AccountService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity register(@RequestBody @Validated RegisterRequest request)
      throws UserAlreadyRegisteredException {
    accountService.register(request);
    return ResponseEntity.ok(AppResponse.successful());
  }

  @PostMapping("/login")
  public AppResponse login(@RequestBody @Validated LoginRequest request) {
    try {
      var token = new UsernamePasswordAuthenticationToken(request.email(), request.password());
      Authentication authentication = authenticationProvider.authenticate(token);
      boolean authenticated = authentication.isAuthenticated();
      if (!authenticated) {
        throw new BadCredentialsException("Bad credentials!");
      }
      SecurityContextHolder.getContext().setAuthentication(authentication);
      return AppResponses.successful();
    } catch (BadCredentialsException ex) {
      return AppResponses.failure("5000", "Authentication failed.",
                                  "Credentials are not valid. Consider resetting your password.");
    }
  }

  @PostMapping("/exit")
  public ResponseEntity logout(HttpServletRequest request) throws ServletException {
    request.logout();
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public AppResponse resetPassword(@RequestBody @Validated ResetPasswordRequest request) {
    accountService.resetPassword(request.email());
    return AppResponse.successful();
  }

  @PostMapping("/reset-password/token")
  public AppResponse resetPasswordWithToken(@RequestBody @Validated ResetPasswordWithTokenRequest request) {
    accountService.resetPasswordWithToken(request.token(), request.password());
    return AppResponse.successful();
  }
}
