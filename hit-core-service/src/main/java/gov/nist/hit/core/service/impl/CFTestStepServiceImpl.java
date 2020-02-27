package gov.nist.hit.core.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.repo.CFTestStepRepository;
import gov.nist.hit.core.service.CFTestStepService;

@Service
public class CFTestStepServiceImpl implements CFTestStepService {

  @Autowired
  private CFTestStepRepository testStepRepository;

  @Override
  public CFTestStep findOne(Long id) {
    return testStepRepository.findOne(id);
  }

  @Override
  public void delete(Long id) {
    testStepRepository.delete(id);
  }

  @Override
  public void save(CFTestStep testStep) {
	testStep.updateUpdateDate();
    testStepRepository.saveAndFlush(testStep);
  }

  @Override
  public void delete(CFTestStep testStep) {
    testStepRepository.delete(testStep);
  }
  
  @Override
  public Date getUpdateDate(Long id) {
	  return testStepRepository.getUpdateDate(id);
  }


}
