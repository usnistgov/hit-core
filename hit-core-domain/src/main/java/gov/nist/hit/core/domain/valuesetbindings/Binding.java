package gov.nist.hit.core.domain.valuesetbindings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

  

public class Binding   {


	public Binding() {
		super();
	}
	
	public Binding(String bindingIdentifier, Boolean isExternal ) {
		super();
		this.bindingIdentifier = bindingIdentifier;
		this.isExternal = isExternal;
	}


 	protected String bindingIdentifier;
 	protected Boolean isExternal;


	public String getBindingIdentifier() {
		return bindingIdentifier;
	}


	public void setBindingIdentifier(String bindingIdentifier) {
		this.bindingIdentifier = bindingIdentifier;
	}

	public Boolean getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}


	


	
	
	
	
}
