package com.infrasave.config;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author huseyinaydin
 */
public class CustomUserDetails implements UserDetails {

  private Long userId;

  private String username;

  private String password;

  private List<SimpleGrantedAuthority> authorities;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;

  public CustomUserDetails(Long userId, String username, String password, List<SimpleGrantedAuthority> authorities,
                           boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired,
                           boolean enabled) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.enabled = enabled;
  }

  public Long getUserId() {
    return userId;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
