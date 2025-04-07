package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class AssertionSlicing {
	
	private Integer position;
	private List<AssertionSlice> slices = new ArrayList<AssertionSlice>();
	
	public AssertionSlicing() {
		super();
	}

	
	public AssertionSlicing(Integer position) {
		super();
		this.position = position;
	}


	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public List<AssertionSlice> getSlices() {
		return slices;
	}

	public void setSlices(List<AssertionSlice> slices) {
		this.slices = slices;
	}

	

}
