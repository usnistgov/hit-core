package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class AssertionSlice {
	
	private String ref;	
	private String Description;
	private String Assertion;
	
	
	public AssertionSlice() {
		super();
	}
	
	

	public AssertionSlice(String ref) {
		super();
		this.ref = ref;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getAssertion() {
		return Assertion;
	}

	public void setAssertion(String assertion) {
		Assertion = assertion;
	}

	public String getRef() {
		return ref;
	}



	public void setRef(String ref) {
		this.ref = ref;
	}



	

}
