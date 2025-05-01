package gov.nist.hit.core.domain.valuesetbindings;

import java.io.Serializable;

//@Entity
//@Table(name = "CONSTRAINTS")
public class ValueSetBindings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private Context datatypes = new Context();

	private Context segments = new Context();

	private Context groups = new Context();

	private Context messages = new Context();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Context getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(Context datatypes) {
		this.datatypes = datatypes;
	}

	public Context getSegments() {
		return segments;
	}

	public void setSegments(Context segments) {
		this.segments = segments;
	}

	public Context getGroups() {
		return groups;
	}

	public void setGroups(Context groups) {
		this.groups = groups;
	}

	public Context getMessages() {
		return messages;
	}

	public void setMessages(Context messages) {
		this.messages = messages;
	}

}
