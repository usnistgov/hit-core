package gov.nist.hit.core.domain.singlecodebindings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

  

public class ComplexBindingLocation extends BindingLocation{


	public ComplexBindingLocation() {		
		super();
		this.type = "ComplexBindingLocation";
	}
	
	public ComplexBindingLocation(String codeLocation, String codeSystemLocation, String codeSystemOIDLocation ) {
		super();
		this.codeLocation = codeLocation;
		this.codeSystemLocation = codeSystemLocation;
		this.codeSystemOIDLocation = codeSystemOIDLocation;		
	}


	protected String codeSystemLocation;

	protected String codeSystemOIDLocation;

	public String getCodeSystemLocation() {
		return codeSystemLocation;
	}

	public void setCodeSystemLocation(String codeSystemLocation) {
		this.codeSystemLocation = codeSystemLocation;
	}

	public String getCodeSystemOIDLocation() {
		return codeSystemOIDLocation;
	}

	public void setCodeSystemOIDLocation(String codeSystemOIDLocation) {
		this.codeSystemOIDLocation = codeSystemOIDLocation;
	}

	
	
	
	
}
