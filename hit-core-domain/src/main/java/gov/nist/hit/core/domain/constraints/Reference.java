package gov.nist.hit.core.domain.constraints;

//@Embeddable
public class Reference implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private String generatedBy;

	private String referencePath;
	
	private String source;
	
	private String testDataCategorization;
	
	private String chapter;

	private String Section;
	
	private String page;
	
	private String url;

	
	public Reference() {
		super();
	}

	public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	public String getReferencePath() {
		return referencePath;
	}

	public void setReferencePath(String referencePath) {
		this.referencePath = referencePath;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTestDataCategorization() {
		return testDataCategorization;
	}

	public void setTestDataCategorization(String testDataCategorization) {
		this.testDataCategorization = testDataCategorization;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getSection() {
		return Section;
	}

	public void setSection(String section) {
		Section = section;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



}
