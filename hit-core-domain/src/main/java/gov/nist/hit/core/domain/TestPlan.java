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
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import gov.nist.hit.core.domain.util.Views;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TestPlan", description = "Data Model representing a test plan")
@Entity
public class TestPlan extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 8324105895492403037L;

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Views.NoData.class)
  private Long id;

  @ApiModelProperty(required = false, value = "summary of the test plan")
  @JsonIgnoreProperties(value = {"html", "json"})
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
  @JsonView(Views.NoData.class)
  protected TestArtifact testPlanSummary;

  @ApiModelProperty(required = false, value = "test package of the test plan")
  @JsonIgnoreProperties(value = {"html", "json"})
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
  @JsonView(Views.NoData.class)
  protected TestArtifact testPackage;


  public TestPlan() {
	this.updateDate = new Date();
    this.type = ObjectType.TestPlan;
  }
  
  public TestPlan(Long id) {
	this.id = id;
	this.updateDate = new Date();
    this.type = ObjectType.TestPlan;
  }

  @ApiModelProperty(required = false, value = "list of test cases of the test plan")
  @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tp_tc",
      joinColumns = {@JoinColumn(name = "testplan_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "testcase_id", referencedColumnName = "id")})
  @JsonView(Views.NoData.class)
  private Set<TestCase> testCases = new HashSet<TestCase>();

  @ApiModelProperty(required = false, value = "list of test case groups of the test plan")
  @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "tp_tcg",
      joinColumns = {@JoinColumn(name = "testplan_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "testcasegroup_id", referencedColumnName = "id")})
  @JsonView(Views.NoData.class)
  private Set<TestCaseGroup> testCaseGroups = new HashSet<TestCaseGroup>();

  @ApiModelProperty(required = true, value = "transport support of the test plan")
  @JsonView(Views.NoData.class)
  private boolean transport;



  public TestPlan(Long id, String name, String description, int position, boolean transport,
      String domain, Long persistentId) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.position = position;
    this.transport = transport;
    this.domain = domain;
    this.persistentId = persistentId;
    this.updateDate = new Date();
    this.type = ObjectType.TestPlan;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void addTestCase(TestCase testCase) {
    this.testCases.add(testCase);
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

  public boolean isTransport() {
    return transport;
  }

  public void setTransport(boolean transport) {
    this.transport = transport;
  }

  public TestArtifact getTestPlanSummary() {
    return testPlanSummary;
  }

  public void setTestPlanSummary(TestArtifact testPlanSummary) {
    this.testPlanSummary = testPlanSummary;
  }

  public TestArtifact getTestPackage() {
    return testPackage;
  }

  public void setTestPackage(TestArtifact testPackage) {
    this.testPackage = testPackage;
  }
 


}
