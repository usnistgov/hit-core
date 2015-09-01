package gov.nist.hit.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.codehaus.jackson.map.annotate.JsonView;

import com.fasterxml.jackson.annotation.JsonIgnore;
 
/**
 * 
 * @author haffo
 *
 */
@MappedSuperclass
public abstract class AbstractTestCase {

  @Column(columnDefinition = "TEXT")
  protected String name;
  
  @Column
  private String parentName;

  
  @Column(columnDefinition = "TEXT")
  protected String description;
  
  @Enumerated(EnumType.STRING)
  protected TestType type;
  
  @Enumerated(EnumType.STRING)
  protected TestCategory category;
  
  @Enumerated(EnumType.STRING)
  protected Stage stage;
  
  protected int position; 
  
  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = true)
  protected TestArtifact testPackage; 
  
  @JsonIgnore
   @OneToOne(cascade = CascadeType.ALL, optional = true)
  protected TestArtifact testStory; 

  @JsonIgnore
   @OneToOne(cascade = CascadeType.ALL, optional = true)
  protected TestArtifact jurorDocument;
  
  @JsonIgnore
   @OneToOne(cascade = CascadeType.ALL, optional = true)
  protected TestArtifact messageContent;
  
  @JsonIgnore
   @OneToOne(cascade = CascadeType.ALL, optional = true)
  protected TestArtifact testDataSpecification;
   
 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TestType getType() {
    return type;
  }

  public void setType(TestType type) {
    this.type = type;
  }

  public TestCategory getCategory() {
    return category;
  }

  public void setCategory(TestCategory category) {
    this.category = category;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public TestArtifact getTestPackage() {
    return testPackage;
  }

  public void setTestPackage(TestArtifact testPackage) {
    this.testPackage = testPackage;
  }

  

  public TestArtifact getTestStory() {
    return testStory;
  }

  public void setTestStory(TestArtifact testStory) {
    this.testStory = testStory;
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

  public String getParentName() {
    return parentName;
  }

  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

  
  
  
 
}