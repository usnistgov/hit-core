package gov.nist.hit.core.domain.valuesetbindings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

  

public class ValueSetBinding implements Serializable, Cloneable {

	private static final long serialVersionUID = 5723342171557075960L;

	public ValueSetBinding() {
		super();
		this.id = UUID.randomUUID().toString();
	}


 	protected String id;

	protected String valueSetBindingId;

	protected String bindingStrength;

	protected String valueSetBindingTarget;
 
	//xml code
	protected String bindingLocations;

	//xml code
	protected String bindings;
	
	protected List<BindingLocation> bindingLocationList = new ArrayList<BindingLocation>();
	
	protected List<Binding> bindingList = new ArrayList<Binding>();
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValueSetBindingId() {
		return valueSetBindingId;
	}

	public void setValueSetBindingId(String valueSetBindingId) {
		this.valueSetBindingId = valueSetBindingId;
	}

	public String getValueSetBindingTarget() {
		return valueSetBindingTarget;
	}

	public void setValueSetBindingTarget(String valueSetBindingTarget) {
		this.valueSetBindingTarget = valueSetBindingTarget;
	}

	public String getBindingLocations() {
		return bindingLocations;
	}

	public void setBindingLocations(String bindingLocations) {
		this.bindingLocations = bindingLocations;
	}

	public String getBindings() {
		return bindings;
	}

	public void setBindings(String bindings) {
		this.bindings = bindings;
	}	

	public String getBindingStrength() {
		return bindingStrength;
	}

	public void setBindingStrength(String bindingStrength) {
		this.bindingStrength = bindingStrength;
	}		
			
	public List<BindingLocation> getBindingLocationList() {
		return bindingLocationList;
	}

	public void setBindingLocationList(List<BindingLocation> bindingLocationList) {
		this.bindingLocationList = bindingLocationList;
	}

	public List<Binding> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<Binding> bindingList) {
		this.bindingList = bindingList;
	}
	
	public List<Binding> getExternalBindingList() {
		List<Binding> filteredList = bindingList.stream()
		        .filter(obj -> obj.getIsExternal())
		        .collect(Collectors.toList());
		return filteredList;
	}
	

	@Override
	public String toString() {
		return "ValueSetBinding [id=" + id + ", valueSetBindingId=" + valueSetBindingId
				+ ", valueSetBindingTarget=" + valueSetBindingTarget +  ", bindingLocations=" + bindingLocations + ", bindings="
				+ bindings + "]";
	}

	@Override
	protected ValueSetBinding clone() throws CloneNotSupportedException {
		ValueSetBinding c = (ValueSetBinding) super.clone();
		c.setId(this.id);
		return c;
	}

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((valueSetBindingId == null) ? 0 : valueSetBindingId.hashCode());
    result = prime * result + ((valueSetBindingTarget == null) ? 0 : valueSetBindingTarget.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ValueSetBinding other = (ValueSetBinding) obj;
    if (valueSetBindingId == null) {
      if (other.valueSetBindingId != null)
        return false;
    } else if (!valueSetBindingId.equals(other.valueSetBindingId))
      return false;
    if (valueSetBindingTarget == null) {
      if (other.valueSetBindingTarget != null)
        return false;
    } else if (!valueSetBindingTarget.equals(other.valueSetBindingTarget))
      return false;
    return true;
  }
	
	
	
	
}
