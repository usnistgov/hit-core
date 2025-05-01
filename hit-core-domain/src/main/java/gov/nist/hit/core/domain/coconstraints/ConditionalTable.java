package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class ConditionalTable {

	private CoConstraintGroupId coConstraintGroupId;
	private Condition condition;
	
	private List<CoConstraint> coConstraints = new ArrayList<CoConstraint>();
	private List<CoConstraintGroup> coConstraintGroups = new ArrayList<CoConstraintGroup>();

	
	public ConditionalTable() {
		
	}


	public CoConstraintGroupId getCoConstraintGroupId() {
		return coConstraintGroupId;
	}

	public void setCoConstraintGroupId(CoConstraintGroupId coConstraintGroupId) {
		this.coConstraintGroupId = coConstraintGroupId;
	}

	
	public Condition getCondition() {
		return condition;
	}


	public void setCondition(Condition condition) {
		this.condition = condition;
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
