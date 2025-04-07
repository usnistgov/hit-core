package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class CoConstraintContext {

	private String id;
	
	private List<ByMessage> byMessages = new ArrayList<ByMessage>();
	

	public CoConstraintContext() {
		super();
	}
	
	public CoConstraintContext(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ByMessage> getByMessages() {
		return byMessages;
	}

	public void setByMessages(List<ByMessage> byMessages) {
		this.byMessages = byMessages;
	}  
	
	public void addByMessage(ByMessage m) {
		this.byMessages.add(m);
	}
	
}
