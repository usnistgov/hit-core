package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class CoConstraint {

	
	private List<CellsType> selectors = new ArrayList<CellsType>();
	private List<CellsType> constraints = new ArrayList<CellsType>();

	private String usage;
	private String min;
	private String max;
	
	public CoConstraint() {
		
	}

	public List<CellsType> getSelectors() {
		return selectors;
	}

	public void setSelectors(List<CellsType> selectors) {
		this.selectors = selectors;
	}

	public List<CellsType> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<CellsType> constraints) {
		this.constraints = constraints;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}


	


	

	

	
	
	
}
