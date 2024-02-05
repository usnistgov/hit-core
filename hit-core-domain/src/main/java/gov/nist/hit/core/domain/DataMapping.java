package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 * <p/>
 * Created by Maxence Lefort on 1/28/16.
 */
@Entity
public class DataMapping implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @NotNull
  @ManyToOne(cascade = CascadeType.ALL)
  protected MappingSource source;

  @NotNull
  @OneToOne(cascade = CascadeType.ALL)
  protected TestStepFieldPair target;

  @Column
  protected Boolean optional;

  @JsonIgnore

  @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  protected TestCase testCase;

  public DataMapping() {
    super();
  }

  public DataMapping(MappingSource source, TestStepFieldPair target, TestCase testCase,
      Boolean optional) {
    this.source = source;
    this.target = target;
    this.testCase = testCase;
    this.optional = optional;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MappingSource getSource() {
    return source;
  }

  public void setSource(TestStepFieldPair source) {
    this.source = source;
  }

  public TestStepFieldPair getTarget() {
    return target;
  }

  public void setTarget(TestStepFieldPair target) {
    this.target = target;
  }

  public TestCase getTestCase() {
    return testCase;
  }

  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }

  public Boolean getOptional() {
    return optional;
  }

  public void setOptional(Boolean optional) {
    this.optional = optional;
  }

  @Override
  public String toString() {
    return "DataMapping{" + "id=" + id + ", source=" + source + ", target=" + target + ", testCase="
        + testCase + ", optional=" + optional + '}';
  }
}
