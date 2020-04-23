package gov.nist.auth.hit.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import gov.nist.hit.core.domain.ReportType;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestingStage;




public class Report {
	
	private Long id;
    
    private String name;
    
    private String path;
    
    private Double version;
    
	private TestResult result;
	
	private TestingStage stage;
    
    private List<Report> reports;
    
    private Long accountId;
    
    private Long persistentId;
    
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    private Date creationDate;
    
    private ReportType type;
    
	protected String domain;

	
	public Report(Long id, String name,String path, TestingStage stage, ReportType type, Double version, TestResult result, Long accountId,
			Long persistentId, Date creationDate, String domain) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
		this.stage = stage;
		this.type = type;
		this.version = version;
		this.result = result;
		this.accountId = accountId;
		this.persistentId = persistentId;
		this.creationDate = creationDate;
		this.domain = domain;
		this.reports = new ArrayList<Report>();
	}
	
	public Report(Long id, String name, String path, TestingStage stage, ReportType type, Double version, TestResult result, List<Report> reports, Long accountId,
			Long persistentId, Date creationDate, String domain) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
		this.stage = stage;
		this.type = type;
		this.version = version;
		this.result = result;
		this.reports = reports;
		this.accountId = accountId;
		this.persistentId = persistentId;
		this.creationDate = creationDate;
		this.domain = domain;
	}

	public Report() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public TestResult getResult() {
		return result;
	}

	public void setResult(TestResult result) {
		this.result = result;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getPersistentId() {
		return persistentId;
	}

	public void setPersistentId(Long persistentId) {
		this.persistentId = persistentId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public TestingStage getStage() {
		return stage;
	}

	public void setStage(TestingStage stage) {
		this.stage = stage;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

  
	
	
	
    
    
}
