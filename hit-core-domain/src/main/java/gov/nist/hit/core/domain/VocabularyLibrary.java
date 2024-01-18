package gov.nist.hit.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "VocabularyLibrary")
public class VocabularyLibrary extends ValidationArtifact implements Serializable {


  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @JsonIgnore

  @NotNull
  @Column(unique = true, length = 100)
  protected String sourceId;

  @JsonIgnore

  protected String name;

  @JsonIgnore

  protected String description;

  @JsonIgnore

  protected String key_;

  @JsonIgnore

  @Column(columnDefinition = "LONGTEXT")
  protected String xml;

  @JsonIgnore
  @Column(columnDefinition = "LONGTEXT")
  protected String json;



  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }



  public VocabularyLibrary(String valueSetXml) {
    super();
    this.xml = valueSetXml;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

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

  public String getKey() {
    return key_;
  }

  public void setKey(String key) {
    this.key_ = key;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public VocabularyLibrary() {
    super();
    // TODO Auto-generated constructor stub
  }

}
