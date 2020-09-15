package gov.nist.hit.core.service;

import java.io.Reader;

import gov.nist.hit.core.domain.ValidationClassifications;

public interface ValidationConfigurationService {

	public ValidationClassifications getClassifications(String domain_);
	
	public ValidationClassifications getDefaultClassifications();

	public boolean saveClassifications(ValidationClassifications validationClassification, String domain);
	
}
