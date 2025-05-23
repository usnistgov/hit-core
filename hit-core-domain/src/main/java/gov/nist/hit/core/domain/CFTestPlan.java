package gov.nist.hit.core.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import gov.nist.hit.core.domain.util.Views;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class CFTestPlan extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 880596750847898513L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  // @ApiModelProperty(required = true, value = "category of the test plan",
  // example = "CDC, AIRA etc...")
  // @Column(nullable = true)
  // private String category;

  public CFTestPlan() {
    super();
    this.type = ObjectType.TestPlan;
    this.stage = TestingStage.CF;
    this.updateDate = new Date();
  }
  
  public CFTestPlan(Long id) {
	    super();
	    this.id = id;
	    this.type = ObjectType.TestPlan;
	    this.stage = TestingStage.CF;
	    this.updateDate = new Date();
	  }

  public CFTestPlan(Long id, String name, String description, int position, Long persistentId,
      String domain) {
    super();
    this.type = ObjectType.TestPlan;
    this.stage = TestingStage.CF;
    this.id = id;
    this.name = name;
    this.description = description;
    this.position = position;
    this.persistentId = persistentId;
    this.domain = domain;
    this.updateDate = new Date();
  }

  @JsonView(Views.NoData.class)
  @ApiModelProperty(required = false, value = "list of test steps of the test plan")
  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "testplan_teststep",
      joinColumns = {@JoinColumn(name = "testplan_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "teststep_id", referencedColumnName = "id")})
  private Set<CFTestStep> testSteps = new HashSet<CFTestStep>();

  @JsonView(Views.NoData.class)
  @ApiModelProperty(required = false, value = "list of test steps groups of the test plan")
  @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinTable(name = "testplan_teststepgroup",
      joinColumns = {@JoinColumn(name = "testplan_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "teststepgroup_id", referencedColumnName = "id")})
  private Set<CFTestStepGroup> testStepGroups = new HashSet<CFTestStepGroup>();

  @JsonView(Views.NoData.class)
  @Override
  public String getName() {
    return name;
  }
  @JsonView(Views.NoData.class)
  @Override
  public void setName(String name) {
    this.name = name;
  }
  
  @JsonView(Views.NoData.class)
  @Override
  public String getDescription() {
    return description;
  }
  
  @JsonView(Views.NoData.class)
  @Override
  public void setDescription(String description) {
    this.description = description;
  }



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public Set<CFTestStep> getTestSteps() {
    return this.testSteps;
  }


  public Set<CFTestStepGroup> getTestStepGroups() {
    return this.testStepGroups;
  }


  public void setTestSteps(Set<CFTestStep> testSteps) {
    this.testSteps = testSteps;
  }


  public void setTestStepGroups(Set<CFTestStepGroup> testStepGroups) {
    this.testStepGroups = testStepGroups;
  }
  
  public List<CFTestStep> getAllTestSteps() {
      List<CFTestStep> testSteps = new ArrayList<>();


      testSteps.addAll(getTestSteps());
   

      if (testStepGroups != null) {
          for (CFTestStepGroup testCaseGroup : testStepGroups) {
              testSteps.addAll(testCaseGroup.getAllTestSteps());
          }
      }

      return testSteps;
  }




}
