package gov.nist.hit.core.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ValueSetDefinitions implements java.io.Serializable {

	 private static final long serialVersionUID = 1L;

	  private String grouping;
	  
	  private int position;
	  
	  
	  protected Set<ValueSetDefinition> valueSetDefinitions = new HashSet<ValueSetDefinition>();

	  public Set<ValueSetDefinition> getValueSetDefinitions() {
	    return this.valueSetDefinitions;
	  }
	  
	  public Set<ValueSetDefinition> getValueSetDefinitions(boolean isExternal) {
		  Set<ValueSetDefinition> filteredSet = valueSetDefinitions.stream()
			        .filter(obj -> obj.isExternal() == isExternal) // or !obj.isExternal() to filter out external objects
			        .collect(Collectors.toSet());  
		  return filteredSet;
	  }
	 

	  public void setValueSetDefinitions(Set<ValueSetDefinition> valueSetDefinitions) {
	    this.valueSetDefinitions = valueSetDefinitions;
	  }

	  public void addValueSet(ValueSetDefinition td) {
	    getValueSetDefinitions().add(td);
	  }	  

	  public String getGrouping() {
	    return grouping;
	  }

	  public void setGrouping(String grouping) {
	    this.grouping = grouping;
	  }

	  public int getPosition() {
	    return position;
	  }

	  public void setPosition(int position) {
	    this.position = position;
	  }
  
 
  
  
  
}
