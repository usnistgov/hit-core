package gov.nist.hit.core.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.TestPlanRepository;
import gov.nist.hit.core.service.TestPlanService;

@Service
public class TestPlanServiceImpl implements TestPlanService {

	
	static private Map<Long,TestPlan> cache = new HashMap<Long,TestPlan>();
	
	@Autowired
	private TestPlanRepository testPlanRepository;

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findShortAllByStageAndDomain(TestingStage stage, String domain) {
		return testPlanRepository.findShortAllByStageAndDomain(stage, domain);
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope, String domain) {
		return testPlanRepository.findShortAllByStageAndScopeAndDomain(stage, scope, domain);
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findAllShortByStageAndUsernameAndScopeAndDomain(TestingStage stage, String authorUsername,
			TestScope scope, String domain) {
		return testPlanRepository.findAllShortByStageAndUsernameAndScopeAndDomain(stage, authorUsername, scope, domain);
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestArtifact> findAllTestPackagesByDomain(TestingStage stage, String domain) {
		return testPlanRepository.findAllTestPackagesByDomain(stage, domain);
	}

	@Override
	public void loadAll() {		
		List<Long> listIds = testPlanRepository.findAllTestPlanIds();
		for(Long id : listIds) {
			findOne(id);
		}	
	}
	
	@Override
 	public void deleteAllPreloaded() {
 		List<TestPlan> list = testPlanRepository.getAllPreloaded();
 		for(TestPlan tc : list) {
 			testPlanRepository.delete(tc);
 		}
 		
 	}
	
	
	private TestPlan findTestPlanContainingAbstractTestCase(AbstractTestCase node,AbstractTestCase lookingFor, TestPlan tp) {
		
		if (node instanceof TestStep) {
			
			if (lookingFor instanceof TestStep) {
				if (((TestStep)node).getId().equals(((TestStep)lookingFor).getId())){
					return tp;
				}
			}
			
		}else if (node instanceof TestCase) {
			if (lookingFor instanceof TestCase) {
				if (((TestCase)node).getId().equals(((TestCase)lookingFor).getId())){
					return tp;
				}
			}else {
				for(TestStep testS : ((TestCase)node).getTestSteps()) {
					TestPlan testP = findTestPlanContainingAbstractTestCase(testS, lookingFor,tp);
					if (testP != null ) {
						return testP;
					}
				}	
			}
			
		}else if (node instanceof TestCaseGroup) {
			if (lookingFor instanceof TestCaseGroup) {
				if (((TestCaseGroup)node).getId().equals(((TestCaseGroup)lookingFor).getId())){
					return tp;
				}
			}else {
				for(TestCase testC : ((TestCaseGroup)node).getTestCases()) {
					TestPlan testP =  findTestPlanContainingAbstractTestCase(testC, lookingFor,tp);
					if (testP != null ) {
						return testP;
					}
					
				}
				for(TestCaseGroup testCG : ((TestCaseGroup)node).getTestCaseGroups()) {
					TestPlan testP = findTestPlanContainingAbstractTestCase(testCG, lookingFor,tp);
					if (testP != null ) {
						return testP;
					}
				}
			}
			
		}else if (node instanceof TestPlan) {
			//not very useful here
			if (lookingFor instanceof TestPlan) {
				if (((TestPlan)node).getId().equals(((TestPlan)lookingFor).getId())){
					return tp;
				}
			}else {
				for(TestCase testC : ((TestPlan)node).getTestCases()) {
					TestPlan testP = findTestPlanContainingAbstractTestCase(testC, lookingFor,tp);
					if (testP != null ) {
						return testP;
					}
				}
				for(TestCaseGroup testCG : ((TestPlan)node).getTestCaseGroups()) {
					TestPlan testP = findTestPlanContainingAbstractTestCase(testCG, lookingFor,tp);	
					if (testP != null ) {
						return testP;
					}
				}
			}
			
		}else {
			return null;
		}
		return null;	
	}
	
	
	
	@Override
	public TestPlan findTestPlanContainingAbstractTestCase(AbstractTestCase node) {	
		List<TestPlan> testPlans = testPlanRepository.findAllByStageAndDomain(node.getStage(), node.getDomain());
		for(TestPlan testP : testPlans) {				
			if (findTestPlanContainingAbstractTestCase(testP,node,testP) != null) {
				return testP;
			}
		}	
		return null;
	}

	
private String findFullPathContainingAbstractTestCase(AbstractTestCase node,AbstractTestCase lookingFor) {
		
		if (node instanceof TestStep) {
			
			if (lookingFor instanceof TestStep) {
				if (((TestStep)node).getId().equals(((TestStep)lookingFor).getId())){
					return node.getName();
				}
			}
			
		}else if (node instanceof TestCase) {
			if (lookingFor instanceof TestCase) {
				if (((TestCase)node).getId().equals(((TestCase)lookingFor).getId())){
					return node.getName();
				}
			}else {
				for(TestStep testS : ((TestCase)node).getTestSteps()) {
					String res = findFullPathContainingAbstractTestCase(testS, lookingFor);
					if (res != null) {
						return node.getName()+"/"+res;
					}
				}	
			}			
			
		}else if (node instanceof TestCaseGroup) {
			if (lookingFor instanceof TestCaseGroup) {
				if (((TestCaseGroup)node).getId().equals(((TestCaseGroup)lookingFor).getId())){
					return node.getName();
				}
			}else {
				for(TestCase testC : ((TestCaseGroup)node).getTestCases()) {
					String res = findFullPathContainingAbstractTestCase(testC, lookingFor);	
					if (res != null ) {
						return node.getName()+"/"+res;
					}
				}
				for(TestCaseGroup testCG : ((TestCaseGroup)node).getTestCaseGroups()) {
					String res = findFullPathContainingAbstractTestCase(testCG, lookingFor);
					if (res != null ) {
						return node.getName()+"/"+res;
					}
				}
			}
			
		}else if (node instanceof TestPlan) {
			//not very useful here
			if (lookingFor instanceof TestPlan) {
				if (((TestPlan)node).getId().equals(((TestPlan)lookingFor).getId())){
					return node.getName();
				}
			}else {
				for(TestCase testC : ((TestPlan)node).getTestCases()) {
					String res = findFullPathContainingAbstractTestCase(testC, lookingFor);	
					if (res != null ) {
						return node.getName()+"/"+res;
					}
				}
				for(TestCaseGroup testCG : ((TestPlan)node).getTestCaseGroups()) {
					String res = findFullPathContainingAbstractTestCase(testCG, lookingFor);	
					if (res != null) {
						return node.getName()+"/"+res;
					}
				}
			}
			
		}else {
			return null;
		}
		return null;	
	}

	@Override
	public String findFullPathContainingAbstractTestCase(AbstractTestCase node) {	
		List<TestPlan> testPlans = testPlanRepository.findAllByStageAndScopeAndDomain(node.getStage(),node.getScope(), node.getDomain());
		for(TestPlan testP : testPlans) {
			String path =findFullPathContainingAbstractTestCase(testP,node); 
			if (path != null) {
				return path;
			}
		}	
		return null;
	}
	
		
	
	@Override
	public TestPlan findOne(Long testPlanId) {
		if (cache.get(testPlanId) != null && testPlanRepository.getUpdateDate(testPlanId) != null && cache.get(testPlanId).getUpdateDate() != null) {
			//Rounded to the nearest second to avoid (most) date format conversion issues.
			Date d = DateUtils.round(testPlanRepository.getUpdateDate(testPlanId), Calendar.SECOND);
			Date d2 = DateUtils.round(cache.get(testPlanId).getUpdateDate(), Calendar.SECOND);
			if (d2.compareTo(d)== 0) {
				return cache.get(testPlanId);		
			}else {
				TestPlan tp = testPlanRepository.findOne(testPlanId);
				cache.put(testPlanId, tp);
				return tp;
			}			
		}else {
			TestPlan tp = testPlanRepository.findOne(testPlanId);
			cache.put(testPlanId, tp);
			return tp;
		}
		
	}
	
	@Override
	public Date getUpdateDate(Long testPlanId) {		
		return testPlanRepository.getUpdateDate(testPlanId);		
	}

	@Override
	@Transactional(value = "transactionManager")
	public List<TestPlan> findShortAllByStageAndAuthorAndDomain(TestingStage stage, String authorUsername,
			String domain) {
		return testPlanRepository.findShortAllByStageAndAuthorAndDomain(stage, authorUsername, domain);
	}

	@Override
	public List<TestPlan> findShortAllByStageAndScopeAndAuthorAndDomain(TestingStage stage, TestScope scope,
			String authorUsername, String domain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(value = "transactionManager")
	public void delete(TestPlan testPlan) {
		testPlanRepository.delete(testPlan);
	}

	@Override
	public TestPlan save(TestPlan testPlan) {
		// TODO Auto-generated method stub
		testPlan.updateUpdateDate();
		return testPlanRepository.save(testPlan);
	}

	@Override
	public TestPlan findByPersistentId(Long persistentId) {
		return testPlanRepository.getByPersistentId(persistentId);
	}

	@Override
	public boolean updateScope(TestPlan testPlan, TestScope scope) {
		testPlan.setScope(scope);
		Set<TestCase> testCases = testPlan.getTestCases();
		if (testCases != null) {
			for (TestCase testCase : testCases) {
				updateTestScope(testCase, scope);
			}
		}
		Set<TestCaseGroup> testCaseGroups = testPlan.getTestCaseGroups();
		if (testCaseGroups != null) {
			for (TestCaseGroup testCaseGroup : testCaseGroups) {
				updateTestScope(testCaseGroup, scope);
			}
		}
		save(testPlan);
		return true;
	}

	private void updateTestScope(TestCase testCase, TestScope scope) {
		testCase.setScope(scope);
		Set<TestStep> testSteps = testCase.getTestSteps();
		if (testSteps != null) {
			for (TestStep testStep : testSteps) {
				testStep.setScope(scope);
			}
		}
	}

	private void updateTestScope(TestCaseGroup testCaseGroup, TestScope scope) {
		testCaseGroup.setScope(scope);
		Set<TestCase> testCases = testCaseGroup.getTestCases();
		if (testCases != null) {
			for (TestCase testCase : testCases) {
				updateTestScope(testCase, scope);
			}
		}
	}
	
	

}
