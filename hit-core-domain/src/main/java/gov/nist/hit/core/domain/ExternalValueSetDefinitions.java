package gov.nist.hit.core.domain;

import java.util.HashSet;
import java.util.Set;

public class ExternalValueSetDefinitions implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected Set<ValueSetDefinition> externalValueSetDefinitions = new HashSet<ValueSetDefinition>();

	public Set<ValueSetDefinition> getValueSetDefinitions() {
		return this.externalValueSetDefinitions;
	}

	public void setValueSetDefinitions(Set<ValueSetDefinition> valueSetDefinitions) {
		this.externalValueSetDefinitions = valueSetDefinitions;
	}

	public void addValueSet(ValueSetDefinition td) {
		getValueSetDefinitions().add(td);
	}
}
