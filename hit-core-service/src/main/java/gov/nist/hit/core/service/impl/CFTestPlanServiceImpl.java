package gov.nist.hit.core.service.impl;

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

import com.ibm.icu.util.Calendar;

import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
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
		if (cache.get(testPlanId) != null) {
			//Rounded to the nearest second to avoid (most) date format conversion issues.
			Date d = DateUtils.round(testPlanRepository.getUpdateDate(testPlanId), Calendar.SECOND);
			Date d2 = DateUtils.round(cache.get(testPlanId).getUpdateDate(), Calendar.SECOND);
//			System.out.println(d.getTime() + " - "+ d2.getTime()   );
			if (d2.compareTo(d)== 0) {
//				System.out.println("returning cache");
				return cache.get(testPlanId);		
			}else {
//				System.out.println("fetching new because new date");
				CFTestPlan tp = testPlanRepository.findOne(testPlanId);
				cache.put(testPlanId, tp);
				return tp;
			}			
		}else {
//			System.out.println("fetching new because does not existe");
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



}
