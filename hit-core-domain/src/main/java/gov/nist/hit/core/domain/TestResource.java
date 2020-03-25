package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import gov.nist.hit.core.domain.util.Views;

@MappedSuperclass
public class TestResource implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @JsonView(Views.Short.class)
  @NotNull
  @Column(nullable = false)
  protected String domain;

  @JsonIgnore
  // @NotNull
  // @Column(nullable = false)
  protected String authorUsername;
  
  @JsonView(Views.Short.class)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date updateDate;

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }


  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @JsonView(Views.Short.class)
  protected TestScope scope;

  protected boolean preloaded = false;

  public String getAuthorUsername() {
    return authorUsername;
  }

  public void setAuthorUsername(String authorUsername) {
    this.authorUsername = authorUsername;
  }

  public TestScope getScope() {
    return scope;
  }

  public void setScope(TestScope scope) {
    this.scope = scope;
  }

  public boolean isPreloaded() {
    return preloaded;
  }

  public void setPreloaded(boolean preloaded) {
    this.preloaded = preloaded;
  }

  public Date getUpdateDate() {
	  return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
	  this.updateDate = updateDate;
  }

  public void updateUpdateDate() {
	  this.updateDate = new Date();
  }



}
