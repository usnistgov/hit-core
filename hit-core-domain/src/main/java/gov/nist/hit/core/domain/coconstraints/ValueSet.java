package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

import gov.nist.hit.core.domain.valuesetbindings.ValueSetBinding;

public class ValueSet extends CellsType{

	private String name;
	private String path;
	
	

	private List<ValueSetBinding> valueSetBindings = new ArrayList<ValueSetBinding>();



	public ValueSet() {
	
	}


	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}


	public List<ValueSetBinding> getValueSetBindings() {
		return valueSetBindings;
	}


	public void setValueSetBindings(List<ValueSetBinding> valueSetBinding) {
		this.valueSetBindings = valueSetBinding;
	}
	
	public void addValueSetBinding(ValueSetBinding valueSetBinding) {
		this.valueSetBindings.add(valueSetBinding);
	}





	

	
	
	
}
