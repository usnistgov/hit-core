package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class FieldSlicing {

	
	private List<FieldSlicingContext> segmentContexts = new ArrayList<FieldSlicingContext>();

	public FieldSlicing() {
		super();
	}

	public List<FieldSlicingContext> getSegmentContexts() {
		return segmentContexts;
	}

	public void setSegmentContexts(List<FieldSlicingContext> segmentContexts) {
		this.segmentContexts = segmentContexts;
	}

	
	
}
