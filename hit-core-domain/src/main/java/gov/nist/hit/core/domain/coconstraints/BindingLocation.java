package gov.nist.hit.core.domain.coconstraints;

public class BindingLocation extends CellsType{

	private String position;
	private String codePath;
	private String codePathSystem;
	

	public BindingLocation() {
	
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getCodePath() {
		return codePath;
	}


	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}


	public String getCodePathSystem() {
		return codePathSystem;
	}


	public void setCodePathSystem(String codePathSystem) {
		this.codePathSystem = codePathSystem;
	}



	
	
	
}
