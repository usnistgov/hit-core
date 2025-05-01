package gov.nist.hit.core.domain.valuesetbindings;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Context implements Serializable, Cloneable {

	private static final long serialVersionUID = -3037628238620317355L;


	private String id;
	
	private Set<ByNameOrByID> byNameOrByIDs = new HashSet<ByNameOrByID>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<ByNameOrByID> getByNameOrByIDs() {
		return byNameOrByIDs;
	}

	public void setByNameOrByIDs(Set<ByNameOrByID> byNameOrByIDs) {
		this.byNameOrByIDs = byNameOrByIDs;
	}

	@Override
	public String toString() {
		return "Context [id=" + id + "]";
	}

	@Override
	public Context clone() throws CloneNotSupportedException {
		Context clonedContext = (Context) super.clone();
		clonedContext
				.setByNameOrByIDs(new HashSet<ByNameOrByID>(byNameOrByIDs));
		clonedContext.setId(null);
		return clonedContext;
	}

}
