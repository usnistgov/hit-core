package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class ProfileSlicing {

	private SegmentSlicing segmentSlicing;
	private FieldSlicing fieldSlicing;
	private String id;

	public ProfileSlicing() {
		super();
	}
	

	public ProfileSlicing(String id) {
		super();
		this.id = id;
	}


	
	public SegmentSlicing getSegmentSlicing() {
		return segmentSlicing;
	}


	public void setSegmentSlicing(SegmentSlicing segmentSlicing) {
		this.segmentSlicing = segmentSlicing;
	}


	public FieldSlicing getFieldSlicing() {
		return fieldSlicing;
	}


	public void setFieldSlicing(FieldSlicing fieldSlicing) {
		this.fieldSlicing = fieldSlicing;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
