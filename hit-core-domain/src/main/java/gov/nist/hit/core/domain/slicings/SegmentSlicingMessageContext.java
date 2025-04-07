package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class SegmentSlicingMessageContext {

	private List<SegmentSlicingGroupContext> groupContexts = new ArrayList<SegmentSlicingGroupContext>();
	private String id;

	public SegmentSlicingMessageContext() {
		super();
	}
	

	public SegmentSlicingMessageContext(String id) {
		super();
		this.id = id;
	}


	public List<SegmentSlicingGroupContext> getGroupContexts() {
		return groupContexts;
	}


	public void setGroupContexts(List<SegmentSlicingGroupContext> groupContexts) {
		this.groupContexts = groupContexts;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
