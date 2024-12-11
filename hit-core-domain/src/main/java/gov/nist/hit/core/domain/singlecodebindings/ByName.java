package gov.nist.hit.core.domain.singlecodebindings;


//@Entity
//@Table(name = "BYNAME")
public class ByName extends ByNameOrByID {

	/**
	 *  
	 */
	private static final long serialVersionUID = -7656473923145117910L;


	private String byName;

	public String getByName() {
		return byName;
	}

	public void setByName(String byName) {
		this.byName = byName;
	}

	@Override
	public String toString() {
		return "ByName [id=" + id + ", byName=" + byName + "]";
	}

}
