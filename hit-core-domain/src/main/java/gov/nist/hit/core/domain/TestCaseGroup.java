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
 */
package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import gov.nist.hit.core.domain.util.Views;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TestCaseGroup", description = "Data Model representing a group of test cases")
@Entity
public class TestCaseGroup extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 2555650104975908781L;

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(Views.NoData.class)
  private Long id;



  public TestCaseGroup() {
    super();
    this.type = ObjectType.TestCaseGroup;
    this.updateDate = new Date();
  }

  @ApiModelProperty(required = false, value = "children test case groups of the test case group")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "tcg_tcg", joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "child_id")})
  @JsonView(Views.NoData.class)
  private Set<TestCaseGroup> testCaseGroups = new HashSet<TestCaseGroup>();

  @ApiModelProperty(required = false, value = "children test cases of the test case group")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "tcg_tc", joinColumns = {@JoinColumn(name = "testcasegroup_id")},
      inverseJoinColumns = {@JoinColumn(name = "testcase_id")})
  @JsonView(Views.NoData.class)
  private Set<TestCase> testCases = new HashSet<TestCase>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<TestCase> getTestCases() {
    return testCases;
  }

  public void setTestCases(Set<TestCase> testCases) {
    this.testCases = testCases;
  }

  public Set<TestCaseGroup> getTestCaseGroups() {
    return testCaseGroups;
  }

  public void setTestCaseGroups(Set<TestCaseGroup> testCaseGroups) {
    this.testCaseGroups = testCaseGroups;
  }

}
