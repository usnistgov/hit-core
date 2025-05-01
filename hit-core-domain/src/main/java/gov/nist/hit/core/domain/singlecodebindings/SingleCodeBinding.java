package gov.nist.hit.core.domain.singlecodebindings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

  

public class SingleCodeBinding implements Serializable, Cloneable {

	private static final long serialVersionUID = 5723342171557075961L;

	public SingleCodeBinding() {
		super();
		this.id = UUID.randomUUID().toString();
	}
	//internal ID
 	protected String id;

 	//byID ID
	protected String singleCodeBindingId;

	protected String singleCodeBindingTarget;
 
	protected String singleCodeBindingCode;

	protected String singleCodeBindingCodeSystem;
	
	//xml code
	protected String bindingLocations;
	
	protected List<BindingLocation> bindingLocationList = new ArrayList<BindingLocation>();
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSingleCodeBindingId() {
		return singleCodeBindingId;
	}

	public void setSingleCodeBindingId(String singleCodeBindingId) {
		this.singleCodeBindingId = singleCodeBindingId;
	}

	public String getSingleCodeBindingTarget() {
		return singleCodeBindingTarget;
	}

	public void setSingleCodeBindingTarget(String singleCodeBindingTarget) {
		this.singleCodeBindingTarget = singleCodeBindingTarget;
	}

	public String getBindingLocations() {
		return bindingLocations;
	}

	public void setBindingLocations(String bindingLocations) {
		this.bindingLocations = bindingLocations;
	}
			
	public String getSingleCodeBindingCode() {
		return singleCodeBindingCode;
	}

	public void setSingleCodeBindingCode(String singleCodeBindingCode) {
		this.singleCodeBindingCode = singleCodeBindingCode;
	}

	public String getSingleCodeBindingCodeSystem() {
		return singleCodeBindingCodeSystem;
	}

	public void setSingleCodeBindingCodeSystem(String singleCodeBindingCodeSystem) {
		this.singleCodeBindingCodeSystem = singleCodeBindingCodeSystem;
	}

	public List<BindingLocation> getBindingLocationList() {
		return bindingLocationList;
	}

	public void setBindingLocationList(List<BindingLocation> bindingLocationList) {
		this.bindingLocationList = bindingLocationList;
	}

	

	@Override
	public String toString() {
		return "SingleCodeBinding [id=" + id + ", singleCodeBindingCode=" + singleCodeBindingCode
				+ ", singleCodeBindingCodeSytem=" + singleCodeBindingCodeSystem
				+ ", singleCodeBindingTarget=" + singleCodeBindingTarget +  ", bindingLocations=" + bindingLocations + "]";
	}

	@Override
	protected SingleCodeBinding clone() throws CloneNotSupportedException {
		SingleCodeBinding c = (SingleCodeBinding) super.clone();
		c.setId(this.id);
		return c;
	}

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((singleCodeBindingId == null) ? 0 : singleCodeBindingId.hashCode());
    result = prime * result + ((singleCodeBindingTarget == null) ? 0 : singleCodeBindingTarget.hashCode());
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
    SingleCodeBinding other = (SingleCodeBinding) obj;
    if (singleCodeBindingId == null) {
      if (other.singleCodeBindingId != null)
        return false;
    } else if (!singleCodeBindingId.equals(other.singleCodeBindingId))
      return false;
    if (singleCodeBindingTarget == null) {
      if (other.singleCodeBindingTarget != null)
        return false;
    } else if (!singleCodeBindingTarget.equals(other.singleCodeBindingTarget))
      return false;
    return true;
  }
	
	
	
	
}
