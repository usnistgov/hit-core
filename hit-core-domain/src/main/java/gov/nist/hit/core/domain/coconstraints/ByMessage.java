package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class ByMessage {

	private String id;
	
	private List<Context> contexts = new ArrayList<Context>();

	
	public ByMessage() {
		
	}
	

	public ByMessage(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public List<Context> getContexts() {
		return contexts;
	}


	public void setContexts(List<Context> contexts) {
		this.contexts = contexts;
	}

	public void addContext(Context context) {
		this.contexts.add(context);
	}
	
	
	
}
