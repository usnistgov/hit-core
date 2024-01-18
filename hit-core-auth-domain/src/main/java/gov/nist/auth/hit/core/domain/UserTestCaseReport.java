package gov.nist.auth.hit.core.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonView;

import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.util.Views;

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
 * Created by Maxence Lefort on 9/13/16.
 */
@Entity
public class UserTestCaseReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.NoData.class)
    protected Long id;
    
    @JsonView(Views.NoData.class)
    protected String name;
    
    @JsonView(Views.NoData.class)
    protected String path;
    
    @JsonView(Views.NoData.class)
    private Double version;
    
    @JsonView(Views.NoData.class)
    @Enumerated(EnumType.STRING)
	protected TestingStage stage;
    
    @JsonView(Views.NoData.class)
    @Enumerated(EnumType.STRING)
	protected TestResult result;
    
    @JsonView(Views.NoData.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "testCaseReport", orphanRemoval = true, cascade = {CascadeType.REMOVE,CascadeType.PERSIST, CascadeType.MERGE})
    private Set<UserTestStepReport> userTestStepReports;
    
    @JsonView(Views.NoData.class)
    private Long accountId;
    
    @JsonView(Views.NoData.class)
    private Long testCasePersistentId;
    
    @Column(columnDefinition = "LONGTEXT")
    private String xml;
    
    @JsonView(Views.HTML.class)
    @Column(columnDefinition = "LONGTEXT")
    private String html;
    
    @JsonView(Views.NoData.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @JsonView(Views.NoData.class)
    @NotNull
	@Column(nullable = false)
	protected String domain;

    
    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }
    

    public Set<UserTestStepReport> getUserTestStepReports() {
        return userTestStepReports;
    }

    public void addUserTestStepReport(UserTestStepReport userTestStepReport){
        if(userTestStepReports==null){
            userTestStepReports = new HashSet<>();
        }
        userTestStepReports.add(userTestStepReport);
    }

    public void setUserTestStepReports(Set<UserTestStepReport> userTestStepReports) {
        this.userTestStepReports = userTestStepReports;
    }

    public UserTestCaseReport(String name, String domain,TestingStage stage,Long id, Double version, Long accountId, Long testCasePersistentId, String xml, String html) {
    	this.name = name;
    	this.domain = domain;
    	this.stage = stage;
		this.id = id;
		this.version = version;
		this.accountId = accountId;
		this.testCasePersistentId = testCasePersistentId;
		this.xml = xml;
		this.html = html;
		this.creationDate = new Date();
	}
    
    public UserTestCaseReport() {
        this.creationDate = new Date();
	}
        
	public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getTestCasePersistentId() {
        return testCasePersistentId;
    }

    public void setTestCasePersistentId(Long testCasePersistentId) {
        this.testCasePersistentId = testCasePersistentId;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public TestResult getResult() {
		return result;
	}

	public void setResult(TestResult result) {
		this.result = result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Long getId() {
		return id;
	}
	
	public TestingStage getStage() {
		return stage;
	}

	public void setStage(TestingStage stage) {
		this.stage = stage;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
	
    
    
}
