package gov.nist.auth.hit.core.repo;

import gov.nist.auth.hit.core.domain.UserTestStepReport;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
 * <p/>
 * Created by Maxence Lefort on 9/13/16.
 */
public interface UserTestStepReportRepository extends JpaRepository<UserTestStepReport, Long> {

    @Query("select utsr from UserTestStepReport utsr where utsr.accountId = ?1 and utsr.testStepPersistentId = ?2")
    UserTestStepReport findOneByAccountIdAndTestStepPersistentId(Long accountId,Long testStepPersistentId);
    
    @Query("select utsr from UserTestStepReport utsr where utsr.accountId = ?1 and utsr.domain = ?2")
    List<UserTestStepReport> findAllByAccountIdAndDomain(Long accountId,String domain);

    @Query("select utsr from UserTestStepReport utsr where utsr.accountId = ?1 and utsr.domain = ?2 and utsr.testStepPersistentId = ?3")
	List<UserTestStepReport> findAllByAccountIdAndDomainAndTestStepPersistentId(Long accountId, String domain,	Long testStepPersistentId);

    @Query("select utsr from UserTestStepReport utsr where utsr.accountId = ?1 and utsr.domain = ?2 and utsr.testStepPersistentId = ?3 and utsr.testCaseReport IS NULL")
	List<UserTestStepReport> findIndependantByAccountIdAndDomainAndTestStepPersistentId(Long userId, String domain,
			Long testStepPersistentId);
    
}
