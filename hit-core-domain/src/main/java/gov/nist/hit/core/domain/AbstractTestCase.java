package gov.nist.hit.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import gov.nist.hit.core.domain.util.Views;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author haffo
 * 
 */
@MappedSuperclass
public abstract class AbstractTestCase extends TestResource
    implements Comparable<AbstractTestCase> {

  @ApiModelProperty(required = true, value = "name of the test case")
  @Column(columnDefinition = "TEXT")@JsonView(Views.NoData.class)
  protected String name;

  @JsonSerialize(using = ToStringSerializer.class)
  @NotNull
  @Column(unique = true, nullable = false)
  @JsonView(Views.NoData.class)
  protected Long persistentId;


  @ApiModelProperty(required = false, value = "description of the test")
  @Column(columnDefinition = "LONGTEXT")
  @JsonView(Views.NoData.class)
  protected String description;

  @ApiModelProperty(required = true, value = "type of the test")
  @NotNull
  @Enumerated(EnumType.STRING)
  @JsonView(Views.NoData.class)
  protected ObjectType type;

  @ApiModelProperty(required = false, value = "stage of the test")
  @Enumerated(EnumType.STRING)
  @JsonView(Views.NoData.class)
  protected TestingStage stage;


  @ApiModelProperty(required = true, value = "position of the test")
  @Min(1)
  @JsonView(Views.NoData.class)
  protected int position;

  @ApiModelProperty(required = true, value = "version of the test")
  @JsonView(Views.NoData.class)
  protected Double version;

  @ApiModelProperty(required = true, value = "test story of the test case")
  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
  protected TestArtifact testStory;


  @ApiModelProperty(required = true, value = "Supplement documents")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  protected Set<Document> supplements = new HashSet<Document>();
  
  @Temporal(TemporalType.TIMESTAMP)
  @JsonView(Views.NoData.class)
  protected Date updateDate;
  


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

  public ObjectType getType() {
    return type;
  }

  public void setType(ObjectType type) {
    this.type = type;
  }


  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public TestingStage getStage() {
    return stage;
  }

  public void setStage(TestingStage stage) {
    this.stage = stage;
  }

  public Double getVersion() {
    return version;
  }

  public void setVersion(Double version) {
    this.version = version;
  }

  public TestArtifact getTestStory() {
    return testStory;
  }

  public void setTestStory(TestArtifact testStory) {
    this.testStory = testStory;
  }


  @Override
  public int compareTo(AbstractTestCase o) {
    // TODO Auto-generated method stub
    return this.getPosition() - o.getPosition();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractTestCase other = (AbstractTestCase) obj;
    if (!this.getPersistentId().equals(other.getPersistentId()))
      return false;
    return true;
  }

  public Long getPersistentId() {
    return persistentId;
  }

  public void setPersistentId(Long persistentId) {
    this.persistentId = persistentId;
  }

  public Set<Document> getSupplements() {
    return supplements;
  }

  public void setSupplements(Set<Document> supplements) {
    this.supplements = supplements;
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
