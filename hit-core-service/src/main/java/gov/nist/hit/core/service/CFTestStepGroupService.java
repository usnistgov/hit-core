package gov.nist.hit.core.service;

import java.util.Date;

import gov.nist.hit.core.domain.CFTestStepGroup;

public interface CFTestStepGroupService {

  public CFTestStepGroup findOne(Long id);

  public void delete(Long id);

  public void delete(CFTestStepGroup testStep);

  public void save(CFTestStepGroup testStep);
  
  public Date getUpdateDate(Long id);


}
