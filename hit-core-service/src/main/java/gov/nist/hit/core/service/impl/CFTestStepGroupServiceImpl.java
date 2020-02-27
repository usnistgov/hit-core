package gov.nist.hit.core.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.repo.CFTestStepGroupRepository;
import gov.nist.hit.core.service.CFTestStepGroupService;

@Service
public class CFTestStepGroupServiceImpl implements CFTestStepGroupService {

  @Autowired
  private CFTestStepGroupRepository testStepGroupRepository;

  @Override
  public CFTestStepGroup findOne(Long id) {
    return testStepGroupRepository.findOne(id);
  }

  @Override
  public void delete(Long id) {
    testStepGroupRepository.delete(id);
  }

  @Override
  public void save(CFTestStepGroup testStep) {
	  testStep.updateUpdateDate();
	  testStepGroupRepository.saveAndFlush(testStep);
  }

  @Override
  public void delete(CFTestStepGroup testStep) {
    testStepGroupRepository.delete(testStep);
  }

  @Override
  public Date getUpdateDate(Long id) {
	  return testStepGroupRepository.getUpdateDate(id);
  }


}
