package gov.nist.hit.core.service;

import java.util.Date;
import java.util.List;

import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;

public interface TestPlanService {

	public TestPlan findOne(Long testPlanId);

	public List<TestArtifact> findAllTestPackagesByDomain(TestingStage stage, String domain);

	public List<TestPlan> findShortAllByStageAndDomain(TestingStage stage, String domain);

	public List<TestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope, String domain);

	public List<TestPlan> findShortAllByStageAndAuthorAndDomain(TestingStage stage, String authorUsername,
			String domain);

	public List<TestPlan> findShortAllByStageAndScopeAndAuthorAndDomain(TestingStage stage, TestScope scope,
			String authorUsername, String domain);

	void delete(TestPlan testPlan);

	public TestPlan save(TestPlan testPlan);

	public List<TestPlan> findAllShortByStageAndUsernameAndScopeAndDomain(TestingStage stage, String authorUsername,
			TestScope scope, String domain);

	public TestPlan findByPersistentId(Long persistentId);

	public boolean updateScope(TestPlan testPlan, TestScope scope);

	public Date getUpdateDate(Long testPlanId);

	public void loadAll();


	public TestPlan findTestPlanContainingAbstractTestCase(AbstractTestCase node);

	public String findFullPathContainingAbstractTestCase(AbstractTestCase node);


}
