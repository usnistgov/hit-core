package gov.nist.hit.core.domain.valuesetbindings;

import java.util.ArrayList;
import java.util.List;

 
public abstract class ByNameOrByID implements java.io.Serializable {


	private static final long serialVersionUID = -5212340093784881862L;


	protected String id;


	protected List<ValueSetBinding> valuesetbindings = new ArrayList<ValueSetBinding>();
	

	public List<ValueSetBinding> getConformanceStatements() {
		return valuesetbindings;
	}

	public void setConformanceStatements(
			List<ValueSetBinding> valuesetbindings) {
		this.valuesetbindings = valuesetbindings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public void addValueSetBinding(ValueSetBinding e) {
		valuesetbindings.add(e);
	}

}
