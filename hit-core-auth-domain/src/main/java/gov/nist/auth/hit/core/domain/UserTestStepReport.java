package gov.nist.auth.hit.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.util.Views;

/**
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of their
 * official duties. Pursuant to title 17 Section 105 of the United States Code
 * this software is not subject to copyright protection and is in the public
 * domain. This is an experimental system. NIST assumes no responsibility
 * whatsoever for its use by other parties, and makes no guarantees, expressed
 * or implied, about its quality, reliability, or any other characteristic. We
 * would appreciate acknowledgement if the software is used. This software can
 * be redistributed and/or modified freely provided that any derivative works
 * bear some notice that they are derived from it, and any modified versions
 * bear some notice that they have been modified.
 * <p/>
 * Created by Maxence Lefort on 9/13/16.
 */
@Entity
public class UserTestStepReport {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.NoData.class)
	protected Long id;
	
	@JsonView(Views.NoData.class)
	protected String path;
		
	@JsonView(Views.NoData.class)
	protected String name;

	@JsonView(Views.NoData.class)
	@Enumerated(EnumType.STRING)
	protected TestingStage stage;

	@JsonView(Views.NoData.class)
	@Enumerated(EnumType.STRING)
	protected TestResult result;

	@Column(columnDefinition = "LONGTEXT")
	private String xml;

	@JsonView(Views.HTML.class)
	@Column(columnDefinition = "LONGTEXT")
	private String html;

	@Column(columnDefinition = "LONGTEXT")
	private String json;

	@JsonView(Views.NoData.class)
	private Double version;

	@JsonView(Views.NoData.class)
	private Long accountId;

	@JsonView(Views.NoData.class)
	private Long testStepPersistentId;

	@JsonView(Views.NoData.class)
	@Column(columnDefinition = "LONGTEXT")
	private String comments;

	@JsonView(Views.NoData.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@JsonView(Views.NoData.class)
	@NotNull
	@Column(nullable = false)
	protected String domain;
	
	 @JsonIgnore
	 @ManyToOne(optional = true, fetch = FetchType.EAGER)
	 protected UserTestCaseReport testCaseReport;
	 

	public UserTestStepReport(String name,String path, String domain,TestingStage stage, TestResult result, String xml, String html, String json,
			Double version, Long accountId, Long testStepPersistentId, String comments) {
		this.name = name;
		this.path = path;
		this.domain = domain;
		this.stage = stage;
		this.result = result;
		this.xml = xml;
		this.html = html;
		this.json = json;
		this.version = version;
		this.accountId = accountId;
		this.testStepPersistentId = testStepPersistentId;
		this.comments = comments;
		this.creationDate = new Date();
	}
	
	

	public Long getId() {
		return id;
	}
	
	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public UserTestStepReport() {
		this.creationDate = new Date();
	}

	public TestingStage getStage() {
		return stage;
	}

	public void setStage(TestingStage stage) {
		this.stage = stage;
	}

	public TestResult getResult() {
		return result;
	}

	public void setResult(TestResult result) {
		this.result = result;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getTestStepPersistentId() {
		return testStepPersistentId;
	}

	public void setTestStepPersistentId(Long testStepPersistentId) {
		this.testStepPersistentId = testStepPersistentId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public UserTestCaseReport getTestCaseReport() {
		return testCaseReport;
	}

	public void setTestCaseReport(UserTestCaseReport testCaseReport) {
		this.testCaseReport = testCaseReport;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}




	
	
	

}
