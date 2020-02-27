package gov.nist.hit.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@Entity
@ApiModel(value = "TestArtifact", description = "Data Model representing a test artifact")
public class TestArtifact extends TestResource {

  private static final long serialVersionUID = 1L;

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ApiModelProperty(required = true, value = "name of the artifact")
  private String name;

  @ApiModelProperty(required = false, value = "html representation of the artifact")
  @Column(columnDefinition = "LONGTEXT")
  private String html;

  @ApiModelProperty(required = false, value = "pdf path of the artifact")
  @Column(columnDefinition = "LONGTEXT")
  private String pdfPath;

  @ApiModelProperty(required = false, value = "json representation of the artifact")
  @Column(columnDefinition = "LONGTEXT")
  private String json;



  public TestArtifact() {
    super();
    this.name = null;
    this.updateDate = new Date();
  }

  public TestArtifact(String name) {
    super();
    this.name = name;
    this.updateDate = new Date();
  }

  public String getPdfPath() {
    return pdfPath;
  }

  public void setPdfPath(String pdfPath) {
    this.pdfPath = pdfPath;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }



}
