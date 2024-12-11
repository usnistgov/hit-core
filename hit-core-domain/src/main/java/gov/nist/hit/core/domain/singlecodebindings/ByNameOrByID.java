package gov.nist.hit.core.domain.singlecodebindings;

import java.util.ArrayList;
import java.util.List;

public abstract class ByNameOrByID implements java.io.Serializable {

	private static final long serialVersionUID = -5212340093784881862L;

	protected String id;


	protected List<SingleCodeBinding> singlecodebindings = new ArrayList<SingleCodeBinding>();


	public List<SingleCodeBinding> getSingleCodeBindings() {
		return singlecodebindings;
	}

	public void setSinglecodebindings(List<SingleCodeBinding> singlecodebindings) {
		this.singlecodebindings = singlecodebindings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public void addSingleCodeBinding(SingleCodeBinding scb) {
		singlecodebindings.add(scb);
	}
}
