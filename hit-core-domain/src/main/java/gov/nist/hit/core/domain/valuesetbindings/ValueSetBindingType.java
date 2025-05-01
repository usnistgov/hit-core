package gov.nist.hit.core.domain.valuesetbindings;

import java.io.Serializable;

public enum ValueSetBindingType implements Serializable {
    ByName("ByName"), ByID("ByID");
    
    private String value;

    ValueSetBindingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

