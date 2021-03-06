package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import gov.nist.hit.core.domain.util.Views;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TestStep", description = "Data Model representing a test step")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TestStep extends AbstractTestCase implements Serializable {

  private static final long serialVersionUID = 8805967508478985159L;

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(Views.NoData.class)
  private Long id;


  @ApiModelProperty(required = true, value = "domain of the test step")
  @NotNull
  @Enumerated(EnumType.STRING)
  @JsonView(Views.NoData.class)
  protected TestingType testingType;

  @ApiModelProperty(required = false, value = "juror document of the test step")
  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
  protected TestArtifact jurorDocument;

  @ApiModelProperty(required = false, value = "message content of the test step")
  @JsonIgnore

  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
  protected TestArtifact messageContent;

  @ApiModelProperty(required = false, value = "test data specification of the test step")
  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
  protected TestArtifact testDataSpecification;

  @ApiModelProperty(required = false, value = "supported protocols of the test step")
  @Embedded
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "TestStepProtocols", joinColumns = @JoinColumn(name = "TestStep"))
  @JsonView(Views.NoData.class)
  private Set<Protocol> protocols = new HashSet<Protocol>();


  public TestStep() {
    super();
    this.type = ObjectType.TestStep;
    this.updateDate = new Date();
  }

  @ApiModelProperty(required = false, value = "test context of the test step")
  @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER,
      orphanRemoval = true)
  @JoinColumn(name = "testContext_id")
  protected TestContext testContext;

  @ApiModelProperty(required = false, value = "parent test case of the test step")
  @JsonIgnore
  @ManyToOne(optional = true, fetch = FetchType.EAGER)
  protected TestCase testCase;


  public TestStep(String name) {
    super();
    this.name = name;
    this.updateDate = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TestContext getTestContext() {
    return testContext;
  }

  public void setTestContext(TestContext testContext) {
    this.testContext = testContext;
    this.testContext.setTestStep(this);
  }

  public TestingType getTestingType() {
    return testingType;
  }

  public void setTestingType(TestingType testingType) {
    this.testingType = testingType;
  }


  public TestCase getTestCase() {
    return testCase;
  }

  public void setTestCase(TestCase testCase) {
    this.testCase = testCase;
  }

  public TestArtifact getJurorDocument() {
    return jurorDocument;
  }

  public void setJurorDocument(TestArtifact jurorDocument) {
    this.jurorDocument = jurorDocument;
  }

  public TestArtifact getMessageContent() {
    return messageContent;
  }

  public void setMessageContent(TestArtifact messageContent) {
    this.messageContent = messageContent;
  }

  public TestArtifact getTestDataSpecification() {
    return testDataSpecification;
  }

  public void setTestDataSpecification(TestArtifact testDataSpecification) {
    this.testDataSpecification = testDataSpecification;
  }

  public Set<Protocol> getProtocols() {
    return protocols;
  }

  public void setProtocols(Set<Protocol> protocols) {
    this.protocols = protocols;
  }

}
