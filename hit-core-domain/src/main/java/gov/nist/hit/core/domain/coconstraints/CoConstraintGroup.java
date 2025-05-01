package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class CoConstraintGroup {

	private CoConstraint primary = new CoConstraint();
	private List<CoConstraint> coConstraints = new ArrayList<CoConstraint>();

	private String name;
	private String usage;
	private String min;
	private String max;
	
	public CoConstraintGroup() {
		
	}

	

	public CoConstraint getPrimary() {
		return primary;
	}



	public void setPrimary(CoConstraint primary) {
		this.primary = primary;
	}



	public List<CoConstraint> getCoConstraints() {
		return coConstraints;
	}



	public void setCoConstraints(List<CoConstraint> coConstraints) {
		this.coConstraints = coConstraints;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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
