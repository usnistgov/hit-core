/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nist.hit.core.domain.coconstraints.CellsType;
import gov.nist.hit.core.domain.coconstraints.CoConstraint;
import gov.nist.hit.core.domain.coconstraints.CoConstraintGroup;
import gov.nist.hit.core.domain.coconstraints.ConditionalTable;
import gov.nist.hit.core.domain.coconstraints.Context;
import gov.nist.hit.core.domain.coconstraints.Segment;
import gov.nist.hit.core.domain.coconstraints.SimpleTable;
import gov.nist.hit.core.domain.coconstraints.ValueSet;
import gov.nist.hit.core.domain.singlecodebindings.SingleCodeBinding;
import gov.nist.hit.core.domain.slicings.ProfileSlicing;
import gov.nist.hit.core.domain.valuesetbindings.ValueSetBinding;

/**
 * Tree representation of a integrationProfile
 * 
 * @author Harold Affo (NIST)
 */
public class ProfileModel {
	private ProfileElement message;
	private Map<String, ProfileElement> datatypes;
	private Map<String, ProfileElement> segments; // segments and groups
	private ArrayList<ValueSetBinding> valueSetBinding;
	private ArrayList<SingleCodeBinding> singleCodeBinding;
	private ArrayList<Context> coConstraints;
	private ProfileSlicing profileSlicing;
	
	public ProfileModel() {
	    super();
	    valueSetBinding = new ArrayList<ValueSetBinding>();
	    singleCodeBinding = new ArrayList<SingleCodeBinding>();
	    coConstraints = new ArrayList<Context>();
	  }
	
	public ProfileElement getMessage() {
		return message;
	}

	public void setMessage(ProfileElement message) {
		this.message = message;
	}

	public Map<String, ProfileElement> getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(Map<String, ProfileElement> datatypes) {
		this.datatypes = datatypes;
	}

	public Map<String, ProfileElement> getSegments() {
		return segments;
	}

	public void setSegments(Map<String, ProfileElement> segments) {
		this.segments = segments;
	}

	public void addValueSetBindings(List<ValueSetBinding> valuesetbindings) {
		this.valueSetBinding.addAll(valuesetbindings);
	}

	public ArrayList<ValueSetBinding> getValueSetBinding() {
		return valueSetBinding;
	}

	public void setValueSetBinding(ArrayList<ValueSetBinding> valueSetBinding) {
		this.valueSetBinding = valueSetBinding;
	}

	public void addSingleCodeBindings(List<SingleCodeBinding> singleCodeBindings) {
		this.singleCodeBinding.addAll(singleCodeBindings);
	}
	
	public ArrayList<SingleCodeBinding> getSingleCodeBinding() {
		return singleCodeBinding;
	}

	public void setSingleCodeBinding(ArrayList<SingleCodeBinding> singleCodeBinding) {
		this.singleCodeBinding = singleCodeBinding;
	}

	public ArrayList<Context> getCoConstraints() {
		return coConstraints;
	}

	public void setCoConstraints(ArrayList<Context> coConstraints) {
		this.coConstraints = coConstraints;
	}
	
	
	
	public ProfileSlicing getProfileSlicing() {
		return profileSlicing;
	}

	public void setProfileSlicing(ProfileSlicing profileSlicing) {
		this.profileSlicing = profileSlicing;
	}

	public List<ValueSetBinding> findValueSetBindingsFromCoConstraints() {
		List<ValueSetBinding> list = new ArrayList<ValueSetBinding>();

		for (Context c: this.coConstraints) {
			for(Segment s: c.getSegments()) {
				for(ConditionalTable ctable : s.getConditionalTables()) {
					for(CoConstraint cc : ctable.getCoConstraints()) {
						for(CellsType sel : cc.getSelectors()) {
							if (sel instanceof ValueSet) {
								list.addAll((((ValueSet) sel).getValueSetBindings()));								
							}
						}
						for(CellsType con : cc.getConstraints()) {
							if (con instanceof ValueSet) {
								list.addAll((((ValueSet) con).getValueSetBindings()));								
							}
						}
					}
					for(CoConstraintGroup ccg : ctable.getCoConstraintGroups()) {
						for(CoConstraint cc : ccg.getCoConstraints()) {
							for(CellsType sel : cc.getSelectors()) {
								if (sel instanceof ValueSet) {
									list.addAll((((ValueSet) sel).getValueSetBindings()));								
								}
							}
							for(CellsType con : cc.getConstraints()) {
								if (con instanceof ValueSet) {
									list.addAll((((ValueSet) con).getValueSetBindings()));								
								}
							}
						}
					}
				}
				for(SimpleTable stable : s.getSimpleTables()) {
					for(CoConstraint cc : stable.getCoConstraints()) {
						for(CellsType sel : cc.getSelectors()) {
							if (sel instanceof ValueSet) {
								list.addAll((((ValueSet) sel).getValueSetBindings()));								
							}
						}
						for(CellsType con : cc.getConstraints()) {
							if (con instanceof ValueSet) {
								list.addAll((((ValueSet) con).getValueSetBindings()));								
							}
						}
					}
					for(CoConstraintGroup ccg : stable.getCoConstraintGroups()) {
						for(CoConstraint cc : ccg.getCoConstraints()) {
							for(CellsType sel : cc.getSelectors()) {
								if (sel instanceof ValueSet) {
									list.addAll((((ValueSet) sel).getValueSetBindings()));								
								}
							}
							for(CellsType con : cc.getConstraints()) {
								if (con instanceof ValueSet) {
									list.addAll((((ValueSet) con).getValueSetBindings()));								
								}
							}
						}
					}
				}
				
			}
		}
		return list;
	}
	
	
	

}
