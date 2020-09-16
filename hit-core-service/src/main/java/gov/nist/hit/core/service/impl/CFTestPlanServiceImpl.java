package gov.nist.hit.core.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.repo.CFTestPlanRepository;
import gov.nist.hit.core.service.CFTestPlanService;

@Service
public class CFTestPlanServiceImpl implements CFTestPlanService {

  @Autowired
  private CFTestPlanRepository testPlanRepository;

  @Autowired
  @PersistenceContext(unitName = "base-tool")
  protected EntityManager entityManager;

  static private Map<Long,CFTestPlan> cache = new HashMap<Long,CFTestPlan>();


  @Override
  public List<CFTestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndScopeAndDomain(stage, scope, domain);
  }

  @Override
  public List<CFTestPlan> findShortAllByStageAndAuthorAndScopeAndDomain(TestingStage stage,
      String authorUsername, TestScope scope, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByStageAndAuthorAndScopeAndDomain(stage, authorUsername,
        scope, domain);
  }

  @Override
	public void loadAll() {
		List<Long> listIds = testPlanRepository.findAllTestPlanIds();
		for(Long id : listIds) {
			findOne(id);
		}	
	}
    
  @Override
	public CFTestPlan findOne(Long testPlanId) {
		if (cache.get(testPlanId) != null && testPlanRepository.getUpdateDate(testPlanId) != null && cache.get(testPlanId).getUpdateDate() != null) {
			//Rounded to the nearest second to avoid (most) date format conversion issues.			
			Date d = DateUtils.round(testPlanRepository.getUpdateDate(testPlanId), Calendar.SECOND);
			Date d2 = DateUtils.round(cache.get(testPlanId).getUpdateDate(), Calendar.SECOND);
			if (d2.compareTo(d)== 0) {
				return cache.get(testPlanId);		
			}else {
				CFTestPlan tp = testPlanRepository.findOne(testPlanId);
				cache.put(testPlanId, tp);
				return tp;
			}			
		}else {
			CFTestPlan tp = testPlanRepository.findOne(testPlanId);
			cache.put(testPlanId, tp);
			return tp;
		}		
	}
  

  @Override
  public List<CFTestPlan> findAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllByStageAndScopeAndDomain(stage, scope, domain);
  }

  @Override
  public List<CFTestPlan> findAllByScopeAndDomain(TestScope scope, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findAllByScopeAndDomain(scope, domain);
  }


  @Override
  public List<CFTestPlan> findByIds(Set<Long> ids) {
    // TODO Auto-generated method stub
    return testPlanRepository.findByIds(ids);
  }

  
	private CFTestPlan findCFTestPlanContainingAbstractTestCase(AbstractTestCase node,AbstractTestCase lookingFor, CFTestPlan tp) {
		
		if (node instanceof CFTestStep) {
			
			if (lookingFor instanceof CFTestStep) {
				if (((CFTestStep)node).getId().equals(((CFTestStep)lookingFor).getId())){
					return tp;
				}
			}
			
		}else if (node instanceof CFTestStepGroup) {
			if (lookingFor instanceof CFTestStepGroup) {
				if (((CFTestStepGroup)node).getId().equals(((CFTestStepGroup)lookingFor).getId())){
					return tp;
				}
			}else {
				for(CFTestStep testS : ((CFTestStepGroup)node).getTestSteps()) {
					CFTestPlan testP = findCFTestPlanContainingAbstractTestCase(testS, lookingFor,tp);	
					if (testP != null ) {
						return testP;
					}
				}	
				for(CFTestStepGroup testCG : ((CFTestStepGroup)node).getTestStepGroups()) {
					CFTestPlan testP = findCFTestPlanContainingAbstractTestCase(testCG, lookingFor,tp);	
					if (testP != null ) {
						return testP;
					}
				}
			}
			
		}else if (node instanceof CFTestPlan) {
			//not very useful here
			if (lookingFor instanceof CFTestPlan) {
				if (((CFTestPlan)node).getId().equals(((CFTestPlan)lookingFor).getId())){
					return tp;
				}
			}else {
				for(CFTestStep testS : ((CFTestPlan)node).getTestSteps()) {
					CFTestPlan testP = findCFTestPlanContainingAbstractTestCase(testS, lookingFor,tp);
					if (testP != null ) {
						return testP;
					}
				}	
				for(CFTestStepGroup testCG : ((CFTestPlan)node).getTestStepGroups()) {
					CFTestPlan testP = findCFTestPlanContainingAbstractTestCase(testCG, lookingFor,tp);		
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
	public CFTestPlan findCFTestPlanContainingAbstractTestCase(AbstractTestCase node) {	
		List<CFTestPlan> testPlans = testPlanRepository.findAllByStageAndScopeAndDomain(node.getStage(), node.getScope(), node.getDomain());
		for(CFTestPlan testP : testPlans) {				
			if (findCFTestPlanContainingAbstractTestCase(testP,node,testP) != null) {
				return testP;
			}
		}	
		return null;
	}

  private String findCFFullPathContainingAbstractTestCase(AbstractTestCase node,AbstractTestCase lookingFor) {
		
		if (node instanceof CFTestStep) {
			
			if (lookingFor instanceof CFTestStep) {
				if (((CFTestStep)node).getId().equals(((CFTestStep)lookingFor).getId())){
					return node.getName();
				}
			}
			
		}else if (node instanceof CFTestStepGroup) {
			if (lookingFor instanceof CFTestStepGroup) {
				if (((CFTestStepGroup)node).getId().equals(((CFTestStepGroup)lookingFor).getId())){
					return node.getName();
				}
			}else {
				for(CFTestStep testS : ((CFTestStepGroup)node).getTestSteps()) {
					String res = findCFFullPathContainingAbstractTestCase(testS, lookingFor);
					if (res != null) {
						return node.getName()+"/"+res;
					}
				}	
				for(CFTestStepGroup testCG : ((CFTestStepGroup)node).getTestStepGroups()) {
					String res = findCFFullPathContainingAbstractTestCase(testCG, lookingFor);		
					if (res != null) {
						return node.getName()+"/"+res;
					}
				}
			}
			
			
		}else if (node instanceof CFTestPlan) {
			//not very useful here
			if (lookingFor instanceof CFTestPlan) {
				if (((CFTestPlan)node).getId().equals(((CFTestPlan)lookingFor).getId())){
					return node.getName();
				}
			}else {
				for(CFTestStep testS : ((CFTestPlan)node).getTestSteps()) {
					String res = findCFFullPathContainingAbstractTestCase(testS, lookingFor);
					if (res != null) {
						return node.getName()+"/"+res;
					}
				}	
				for(CFTestStepGroup testCG : ((CFTestPlan)node).getTestStepGroups()) {
					String res = findCFFullPathContainingAbstractTestCase(testCG, lookingFor);
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
	public String findCFFullPathContainingAbstractTestCase(AbstractTestCase node) {	
		List<CFTestPlan> testPlans = testPlanRepository.findAllByStageAndScopeAndDomain(node.getStage(), node.getScope(), node.getDomain());
		for(CFTestPlan testP : testPlans) {		
			String path = findCFFullPathContainingAbstractTestCase(testP,node);
			if (path != null) {
				return path;
			}
		}	
		return null;
	}

  
  
  @Override
	public Date getUpdateDate(Long testPlanId) {
		return testPlanRepository.getUpdateDate(testPlanId);
	}


  @Override
  public List<CFTestPlan> findShortAllByScopeAndUsernameAndDomain(TestScope scope,
      String authorUsername, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByScopeAndUsernameAndDomain(scope, authorUsername,
        domain);

  }

  @Override
  public List<CFTestPlan> findAllByScopeAndUsernameAndDomain(TestScope scope, String authorUsername,
      String domain) {
    return testPlanRepository.findAllByScopeAndUsernameAndDomain(scope, authorUsername, domain);
  }


  @Override
  public CFTestPlan save(CFTestPlan testPlan) {
	testPlan.updateUpdateDate();
    return testPlanRepository.saveAndFlush(testPlan);
  }



  @Override
  public List<CFTestPlan> findShortAllByScopeAndDomain(TestScope scope, String domain) {
    // TODO Auto-generated method stub
    return testPlanRepository.findShortAllByScopeAndDomain(scope, domain);
  }

  @Override
  public void delete(CFTestPlan testPlan) {
    testPlanRepository.delete(testPlan);

  }

  public boolean removeCacheElement(Long key) {
	  return cache.remove(key) != null;
  }
	
  @Override
 	public void deleteAllPreloaded() {
 		List<CFTestPlan> list = testPlanRepository.getAllPreloaded();
 		for(CFTestPlan tc : list) {
 			testPlanRepository.delete(tc);
 		}
 		
 	}

  


}
