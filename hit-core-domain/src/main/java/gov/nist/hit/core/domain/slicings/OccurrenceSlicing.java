package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class OccurrenceSlicing {
	
	private Integer position;
	private List<OccurrenceSlice> slices = new ArrayList<OccurrenceSlice>();
	
	public OccurrenceSlicing() {
		super();
	}
	
	

	public OccurrenceSlicing(Integer position) {
		super();
		this.position = position;
	}



	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public List<OccurrenceSlice> getSlices() {
		return slices;
	}

	public void setSlices(List<OccurrenceSlice> slices) {
		this.slices = slices;
	}

	

}
