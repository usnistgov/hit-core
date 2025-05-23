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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Class used to carry minimum necessary set of account information when needed by users other than
 * account holder and authorized users.
 * 
 * @author fdevaulx
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortAccount implements Serializable {

  private static final long serialVersionUID = 20130625L;

  private String email;

  private Long id;

  private String fullName;
  private String phone;
  private String employer;
  private String juridiction;
  private String username;
  //not really used
  private String accountType;
  private String title;
  private boolean pending;
  private boolean entityDisabled;
  private Date lastLoggedInDate;
  private Date registrationDate;
  private List<String> authorities;

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }


  /**
   * @return the phone
   */
  public String getPhone() {
    return phone;
  }

  /**
   * @param phone the phone to set
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }



  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  // /**
  // * @return the cehrTechnologies
  // */
  // public List<CehrTechnology> getCehrTechnologies() {
  // return cehrTechnologies == null ? cehrTechnologies = new
  // LinkedList<CehrTechnology>()
  // : cehrTechnologies;
  // }
  //
  // /**
  // * @param cehrTechnologies
  // * the cehrTechnologies to set
  // */
  // public void setCehrTechnologies(List<CehrTechnology> cehrTechnologies) {
  // this.cehrTechnologies = cehrTechnologies;
  // }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmployer() {
    return employer;
  }

  public void setEmployer(String employer) {
    this.employer = employer;
  }

  public String getJuridiction() {
    return juridiction;
  }

  public void setJuridiction(String juridiction) {
    this.juridiction = juridiction;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isPending() {
    return pending;
  }

  public void setPending(boolean pending) {
    this.pending = pending;
  }

  public boolean isEntityDisabled() {
    return entityDisabled;
  }

  public void setEntityDisabled(boolean entityDisabled) {
    this.entityDisabled = entityDisabled;
  }

  public Date getLastLoggedInDate() {
    return lastLoggedInDate;
  }

  public void setLastLoggedInDate(Date lastLoggedInDate) {
    this.lastLoggedInDate = lastLoggedInDate;
  }


  public Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }

	public List<String> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}



}
