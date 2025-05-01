package gov.nist.hit.core.domain.valuesetbindings;


public class ByID extends ByNameOrByID {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1167310291230293964L;


	protected String byID;

	public String getByID() {
		return byID;
	}

	public void setByID(String byID) {
		this.byID = byID;
	}

	@Override
	public String toString() {
		return "ByID [id=" + id + ", byID=" + byID + "]";
	}

}
