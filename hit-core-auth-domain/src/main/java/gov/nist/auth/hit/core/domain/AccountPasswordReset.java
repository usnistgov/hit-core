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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.crypto.codec.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import gov.nist.auth.hit.core.domain.util.UserUtil;

/**
 * @author fdevaulx
 * 
 */
@Entity
@JsonIgnoreProperties(value = "new", ignoreUnknown = true)
public class AccountPasswordReset implements Serializable {

  private static final long serialVersionUID = 20130625L;
  public static final Long tokenValidityTimeInMilis = 1200000L; //20 min


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;


  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String currentToken;

  @Temporal(TemporalType.TIMESTAMP)
  private Date timestamp;
  private Long numberOfReset = 0L;

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
   * @return the currentToken
   */
  public String getCurrentToken() {
    return currentToken;
  }

  /**
   * @param currentToken the currentToken to set
   */
  public void setCurrentToken(String currentToken) {
    this.currentToken = currentToken;
  }

  /**
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @return the numberOfReset
   */
  public Long getNumberOfReset() {
    return numberOfReset;
  }

  /**
   * @param numberOfReset the numberOfReset to set
   */
  public void setNumberOfReset(Long numberOfReset) {
    this.numberOfReset = numberOfReset;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isTokenExpired() {
    boolean result = false;

    Long currentTimeInMilis = (new Date()).getTime();
    result = (currentTimeInMilis - timestamp.getTime()) > tokenValidityTimeInMilis;

    return result;
  }

  public String getNewToken() throws Exception {
//    if (this.getUsername() == null) {
//      throw new Exception("usernameIsNull");
//    }
//
//    String result = this.getUsername() + UserUtil.generateRandom();
//    // base 64 encoding
//    byte[] bs = Base64.encode(result.getBytes());
//
//    result = new String(bs, "UTF-8");
	String result = new String(Base64.encode(UUID.randomUUID().toString().getBytes()));
    return result;
  }

}
