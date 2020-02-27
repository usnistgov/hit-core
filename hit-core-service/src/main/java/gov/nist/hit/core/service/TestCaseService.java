package gov.nist.hit.core.service;

import java.util.Date;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;

public interface TestCaseService {

  public TestCase findOne(Long id);

  public TestArtifact testStory(Long id);

  public void delete(TestCase testCase);

  void save(TestCase testCase);

  public Date getUpdateDate(Long testCaseId);


}
