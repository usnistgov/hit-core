package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class SegmentSlicingGroupContext {

	
	private List<AssertionSlicing> assertionSlicing = new ArrayList<AssertionSlicing>();
	private List<OccurrenceSlicing> occurrenceSlicing = new ArrayList<OccurrenceSlicing>();
	private String id;
	
	public SegmentSlicingGroupContext() {
		super();		
	}
	
	public SegmentSlicingGroupContext(String id) {
		super();
		this.id = id;
	}
	public List<AssertionSlicing> getAssertionSlicing() {
		return assertionSlicing;
	}
	public void setAssertionSlicing(List<AssertionSlicing> assertionSlicing) {
		assertionSlicing = assertionSlicing;
	}
	public List<OccurrenceSlicing> getOccurrenceSlicing() {
		return occurrenceSlicing;
	}
	public void setOccurrenceSlicing(List<OccurrenceSlicing> occurrenceSlicing) {
		this.occurrenceSlicing = occurrenceSlicing;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
