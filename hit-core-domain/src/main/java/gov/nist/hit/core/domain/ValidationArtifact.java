package gov.nist.hit.core.domain;

import java.util.Date;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ValidationArtifact extends TestResource {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ValidationArtifact() {
	  super();
	  this.updateDate = new Date();
	  this.preloaded = true;
  }



}
