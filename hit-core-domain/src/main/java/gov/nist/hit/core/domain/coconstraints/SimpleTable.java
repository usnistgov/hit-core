package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class SimpleTable {

	private CoConstraintGroupId coConstraintGroupId;
	
	private List<CoConstraint> coConstraints = new ArrayList<CoConstraint>();
	private List<CoConstraintGroup> coConstraintGroups = new ArrayList<CoConstraintGroup>();

	
	public SimpleTable() {
		
	}


	public CoConstraintGroupId getCoConstraintGroupId() {
		return coConstraintGroupId;
	}

	public void setCoConstraintGroupId(CoConstraintGroupId coConstraintGroupId) {
		this.coConstraintGroupId = coConstraintGroupId;
	}

	public List<CoConstraint> getCoConstraints() {
		return coConstraints;
	}

	public void setCoConstraints(List<CoConstraint> coConstraints) {
		this.coConstraints = coConstraints;
	}

	public List<CoConstraintGroup> getCoConstraintGroups() {
		return coConstraintGroups;
	}

	public void setCoConstraintGroups(List<CoConstraintGroup> coConstraintGroups) {
		this.coConstraintGroups = coConstraintGroups;
	}



	

	

	
	
	
}
