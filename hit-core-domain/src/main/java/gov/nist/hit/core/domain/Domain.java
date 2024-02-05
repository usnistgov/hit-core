package gov.nist.hit.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonView;

import gov.nist.hit.core.Constant;
import gov.nist.hit.core.domain.util.Views;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "domain" }) })
public class Domain extends TestResource implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonView(Views.Short.class)
	private String name;
	private String homeTitle;
	private boolean disabled = false;

	@Column(columnDefinition = "TEXT")
	private String messageContentInfo;

	@Column(columnDefinition = "TEXT")
	private String homeContent;

	@Column(columnDefinition = "TEXT")
	private String profileInfo;

	@Column(columnDefinition = "TEXT")
	private String valueSetCopyright;

	@Column(columnDefinition = "TEXT")
	private String validationResultInfo;

	@Column(columnDefinition = "TEXT")
	private String validationConfiguration;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "PARTICIPANT_EMAILS", nullable = true)
	private Set<String> participantEmails = new HashSet<String>();

	private String rsbVersion;

	private String igVersion;

	@JsonView(Views.Short.class)
	private String owner;

	@JsonView(Views.Short.class)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "Domain_Options", joinColumns = @JoinColumn(name = "Domain_id"))
	@MapKeyColumn(name = "OPTION_TYPE", length = 100)
	@Column(name = "OPTION_VALUE", length = 100)
	private Map<String, String> options = new HashMap<String, String>();


	
	public Domain() {
		this.owner = this.authorUsername;
		this.updateDate = new Date();
	}

	public Domain(String name, String domain) {
		this.name = name;
		this.domain = domain;
		this.owner = this.authorUsername;
		this.updateDate = new Date();
	}

	public Domain(String name, String domain, TestScope scope, String authorUsername, Set<String> participantEmails) {
		this(name, domain);
		this.scope = scope;
		this.authorUsername = authorUsername;
		this.participantEmails = participantEmails;
		this.owner = authorUsername;
		this.updateDate = new Date();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomeContent() {
		return homeContent;
	}

	public void setHomeContent(String homeContent) {
		this.homeContent = homeContent;
	}

	public String getHomeTitle() {
		return homeTitle;
	}

	public void setHomeTitle(String homeTitle) {
		this.homeTitle = homeTitle;
	}

	public String getMessageContentInfo() {
		return messageContentInfo;
	}

	public void setMessageContentInfo(String messageContentInfo) {
		this.messageContentInfo = messageContentInfo;
	}

	public String getProfileInfo() {
		return profileInfo;
	}

	public void setProfileInfo(String profileInfo) {
		this.profileInfo = profileInfo;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getValueSetCopyright() {
		return valueSetCopyright;
	}

	public void setValueSetCopyright(String valueSetCopyright) {
		this.valueSetCopyright = valueSetCopyright;
	}

	public String getValidationResultInfo() {
		return validationResultInfo;
	}

	public void setValidationResultInfo(String validationResultInfo) {
		this.validationResultInfo = validationResultInfo;
	}

	public Set<String> getParticipantEmails() {
		return participantEmails;
	}

	public void setParticipantEmails(Set<String> participantEmails) {
		this.participantEmails = participantEmails;
	}

	public Long getId() {
		return id;
	}

	public String getRsbVersion() {
		return rsbVersion;
	}

	public void setRsbVersion(String rsbVersion) {
		this.rsbVersion = rsbVersion;
	}

	public String getIgVersion() {
		return igVersion;
	}

	public void setIgVersion(String igVersion) {
		this.igVersion = igVersion;
	}

	public String getOwner() {
		owner = authorUsername;
		return owner;
	}

	public String getValidationConfiguration() {
		return validationConfiguration;
	}

	public void setValidationConfiguration(String validationConfiguration) {
		this.validationConfiguration = validationConfiguration;
	}

	public Map<String, String> getOptions() {
		if (this.options == null) {
			this.options = new HashMap<String, String>();
		}
		return options;
	}


	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	@Transient
	public void setReportSavingSupported(boolean displayed) {
		this.getOptions().put(Constant.REPORT_SAVING_SUPPORTED, Boolean.toString(displayed));
	}

	@Transient
	public Boolean isReportSavingSupported() {
		return this.getOptions().get(Constant.REPORT_SAVING_SUPPORTED) != null
				&& Boolean.valueOf(this.getOptions().get(Constant.REPORT_SAVING_SUPPORTED));
	}

	public void merge(Domain source) {
		this.homeTitle = source.homeTitle;
		this.homeContent = source.homeContent;
		this.disabled = source.disabled;
		this.domain = source.domain;
		this.messageContentInfo = source.messageContentInfo;
		this.name = source.name;
		this.participantEmails = source.participantEmails;
		this.profileInfo = source.profileInfo;
		this.validationResultInfo = source.validationResultInfo;
		this.valueSetCopyright = source.valueSetCopyright;
		this.rsbVersion = source.rsbVersion;
		this.igVersion = source.igVersion;
		this.owner = this.authorUsername;
		this.validationConfiguration = source.validationConfiguration;
		this.options = source.options;
	}



}
