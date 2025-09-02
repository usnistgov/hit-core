package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileSlicing {

	private SegmentSlicing segmentSlicing;
	private FieldSlicing fieldSlicing;
	private Set<String> segmentReferences = new HashSet<String>();
	private Set<String> dataTypeReferences  = new HashSet<String>();
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


	public Set<String> getSegmentReferences() {
		return segmentReferences;
	}


	public void setSegmentReferences(Set<String> segmentReferences) {
		this.segmentReferences = segmentReferences;
	}


	public Set<String> getDataTypeReferences() {
		return dataTypeReferences;
	}


	public void setDataTypeReferences(Set<String> dataTypeReferences) {
		this.dataTypeReferences = dataTypeReferences;
	}


	
	
	

	
}
