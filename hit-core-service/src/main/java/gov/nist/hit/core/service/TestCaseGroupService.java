package gov.nist.hit.core.service;

import java.util.Date;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCaseGroup;

public interface TestCaseGroupService {

  public TestCaseGroup findOne(Long id);

  public TestArtifact testStory(Long id);

  public void delete(TestCaseGroup testCase);

  void save(TestCaseGroup testCaseGroup);
  
  public Date getUpdateDate(Long id);

  public void deleteAllPreloaded();
}
