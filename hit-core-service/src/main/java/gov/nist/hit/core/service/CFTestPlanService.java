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

package gov.nist.hit.core.service;


import java.util.Date;
import java.util.List;
import java.util.Set;

import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;

public interface CFTestPlanService {

  CFTestPlan save(CFTestPlan testPlan);

  CFTestPlan findOne(Long testPlanId);

  List<CFTestPlan> findAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain);

  List<CFTestPlan> findShortAllByStageAndScopeAndDomain(TestingStage stage, TestScope scope,
      String domain);

  List<CFTestPlan> findShortAllByStageAndAuthorAndScopeAndDomain(TestingStage stage,
      String authorUsername, TestScope scope, String domain);

  List<CFTestPlan> findShortAllByScopeAndUsernameAndDomain(TestScope scope, String authorUsername,
      String domain);

  List<CFTestPlan> findShortAllByScopeAndDomain(TestScope scope, String domain);

  List<CFTestPlan> findAllByScopeAndUsernameAndDomain(TestScope scope, String authorUsername, String domain);

  List<CFTestPlan> findAllByScopeAndDomain(TestScope scope, String domain);
  
  

  List<CFTestPlan> findByIds(Set<Long> ids);

  void delete(CFTestPlan testPlan);
  
  public Date getUpdateDate(Long testPlanId);
  
  public TestScope getScope(Long testPlanId);
  
  public String getDomain(Long testPlanId);
  
  void loadAll();
  
  boolean removeCacheElement(Long key);

  public CFTestPlan findCFTestPlanContainingAbstractTestCase(AbstractTestCase node);

  public String findCFFullPathContainingAbstractTestCase(AbstractTestCase node);

  public void deleteAllPreloaded();

  public void deleteAllByDomain(String d);
  

List<CFTestPlan> findShortAllByUsernameAndDomain(String authorUsername, String domain);

List<CFTestPlan> findShortAllByDomain(String domain);

List<CFTestPlan> findAllByUsernameAndDomain(String authorUsername, String domain);

List<CFTestPlan> findAllByDomain(String domain);

}
