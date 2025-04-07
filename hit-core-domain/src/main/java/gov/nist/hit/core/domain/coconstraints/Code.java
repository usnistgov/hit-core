package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class Code extends CellsType{

	private String name;
	private String path;
	private String code;
	private String codeSystem;
	
	

	private List<BindingLocation> bindingLocation = new ArrayList<BindingLocation>();



	public Code() {
	
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getCodeSystem() {
		return codeSystem;
	}



	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}



	public List<BindingLocation> getBindingLocation() {
		return bindingLocation;
	}



	public void setBindingLocation(List<BindingLocation> bindingLocation) {
		this.bindingLocation = bindingLocation;
	}


	

	
	
	
}
