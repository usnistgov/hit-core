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

package gov.nist.hit.core.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;

public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {

  @Deprecated
  @Query("select tp from TestPlan tp where tp.stage = :stage")
  public List<TestPlan> findAllByStage(@Param("stage") TestingStage stage);

  @Query("select new gov.nist.hit.core.domain.TestPlan(id, name, description, position, transport, domain, persistentId) from TestPlan tp where tp.stage = ?1")
  public List<TestPlan> findShortAllByStage(TestingStage stage);

  @Query("select new gov.nist.hit.core.domain.TestPlan(id, name, description, position, transport, domain, persistentId) from TestPlan tp where tp.stage = ?1 and tp.scope = ?2")
  public List<TestPlan> findShortAllByStageAndScope(TestingStage stage, TestScope scope);

  @Query("select new gov.nist.hit.core.domain.TestPlan(id, name, description, position, transport, domain, persistentId) from TestPlan tp where tp.stage = ?1 and tp.authorUsername = ?2")
  public List<TestPlan> findShortAllByStageAndAuthor(TestingStage stage, String authorUsername);


  @Query("select tp.testPackage from TestPlan tp where tp.stage = :stage")
  public List<TestArtifact> findAllTestPackages(@Param("stage") TestingStage stage);

  @Query("select tp.testPlanSummary from TestPlan tp where tp.stage = :stage")
  public List<TestArtifact> findAllTestPlanSummary(@Param("stage") TestingStage stage);

  @Query("select tp.testPlanSummary from TestPlan tp where tp.id = :id")
  public TestArtifact testPlanSummary(@Param("id") Long id);

  @Query("select tp.testPackage from TestPlan tp where tp.id = :id")
  public TestArtifact testPackage(@Param("id") Long id);

  @Query("select tp from TestPlan tp where tp.persistentId = :id")
  public TestPlan getByPersistentId(@Param("id") Long id);


}
