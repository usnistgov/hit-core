package gov.nist.hit.core.domain.coconstraints;

import java.util.ArrayList;
import java.util.List;

public class Segment {

	private String name;
	private String path;
	
	private List<ConditionalTable> conditionalTables = new ArrayList<ConditionalTable>();
	private List<SimpleTable> simpleTables = new ArrayList<SimpleTable>();

	
	public Segment() {
		
	}

	public Segment(String name, String path) {
		this.name = name;
		this.path = path;
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

	public List<ConditionalTable> getConditionalTables() {
		return conditionalTables;
	}

	public void setConditionalTables(List<ConditionalTable> conditionalTables) {
		this.conditionalTables = conditionalTables;
	}

	public List<SimpleTable> getSimpleTables() {
		return simpleTables;
	}

	public void setSimpleTables(List<SimpleTable> simpleTable) {
		this.simpleTables = simpleTable;
	}

	

	

	
	
	
}
