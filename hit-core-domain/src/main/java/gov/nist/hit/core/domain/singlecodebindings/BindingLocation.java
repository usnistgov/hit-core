package gov.nist.hit.core.domain.singlecodebindings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

  

public class BindingLocation   {


	public BindingLocation() {
		super();
	}


 	protected String codeLocation;
 	protected String type;

	public String getCodeLocation() {
		return codeLocation;
	}


	public void setCodeLocation(String codeLocation) {
		this.codeLocation = codeLocation;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	



	
	
	
	
}
