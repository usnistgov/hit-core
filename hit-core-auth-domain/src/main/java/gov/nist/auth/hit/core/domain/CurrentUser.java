/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgment if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.auth.hit.core.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author fdevaulx
 * 
 */
public class CurrentUser {

  private String username;
  private String email;
  private Long accountId;
  private boolean isAuthenticated = false;
  private boolean isPending = false;
  private Collection<GrantedAuthority> authorities;
  private String fullName;
  private boolean guestAccount = false;
  private Long lastTestPlanPersistenceId;
  private String employer;


  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the isAuthenticated
   */
  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  /**
   * @param isAuthenticated the isAuthenticated to set
   */
  public void setAuthenticated(boolean isAuthenticated) {
    this.isAuthenticated = isAuthenticated;
  }

  /**
   * @return the authorities
   */
  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * @param authorities the authorities to set
   */
  public void setAuthorities(Collection<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  /**
   * @return the accountId
   */
  public Long getAccountId() {
    return accountId;
  }

  /**
   * @param accountId the accountId to set
   */
  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }

  public boolean isPending() {
    return isPending;
  }

  public void setPending(boolean isPending) {
    this.isPending = isPending;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public boolean isGuestAccount() {
    return guestAccount;
  }

  public void setGuestAccount(boolean guestAccount) {
    this.guestAccount = guestAccount;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getLastTestPlanPersistenceId() {
    return lastTestPlanPersistenceId;
  }

  public void setLastTestPlanPersistenceId(Long lastTestPlanPersistenceId) {
    this.lastTestPlanPersistenceId = lastTestPlanPersistenceId;
  }

  public String getEmployer() {
    return employer;
  }

  public void setEmployer(String employer) {
    this.employer = employer;
  }

  @Override
  public String toString() {
      return "CurrentUser{" +
              "username='" + username + '\'' +
              ", email='" + email + '\'' +
              ", accountId=" + accountId +
              ", isAuthenticated=" + isAuthenticated +
              ", isPending=" + isPending +
              ", authorities=" + authorities +
              ", fullName='" + fullName + '\'' +
              ", guestAccount=" + guestAccount +
              ", lastTestPlanPersistenceId=" + lastTestPlanPersistenceId +
              ", employer='" + employer + '\'' +
              '}';
  }

}
