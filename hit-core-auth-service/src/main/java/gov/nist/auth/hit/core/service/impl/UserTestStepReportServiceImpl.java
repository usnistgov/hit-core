package gov.nist.auth.hit.core.service.impl;

import gov.nist.auth.hit.core.domain.UserTestCaseReport;
import gov.nist.auth.hit.core.domain.UserTestStepReport;
import gov.nist.auth.hit.core.repo.UserTestStepReportRepository;
import gov.nist.auth.hit.core.service.UserTestStepReportService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service(value = "userTestStepReportService")
public class UserTestStepReportServiceImpl implements UserTestStepReportService {

    @Autowired
    UserTestStepReportRepository userTestStepReportRepository;

    @Override
    public UserTestStepReport findOneByAccountIdAndTestStepPersistentId(Long accountId,Long testStepPersistentId) {
        return userTestStepReportRepository.findOneByAccountIdAndTestStepPersistentId(accountId,testStepPersistentId);
    }       

    @Override
    public UserTestStepReport save(UserTestStepReport userTestStepReport) {
        return userTestStepReportRepository.saveAndFlush(userTestStepReport);
    }

    @Override
    public void delete(UserTestStepReport userTestStepReport) {
        userTestStepReportRepository.delete(userTestStepReport);
    }

	@Override
	public List<UserTestStepReport> findAllByAccountIdAndDomain(Long accountId,String domain) {
		return userTestStepReportRepository.findAllByAccountIdAndDomain(accountId,domain);
	}
	
	@Override
    public UserTestStepReport findOne(Long userTestStepReportId) {
      return userTestStepReportRepository.findOne(userTestStepReportId);
    }

	@Override
	public void delete(Long id) {
		userTestStepReportRepository.delete(id);
	}

	@Override
	public List<UserTestStepReport> findAllByAccountIdAndDomainAndTestStepPersistentId(Long accountId, String domain,Long testStepPersistentId) {
		return userTestStepReportRepository.findAllByAccountIdAndDomainAndTestStepPersistentId(accountId,domain,testStepPersistentId);

	}

	@Override
	public List<UserTestStepReport> findIndependantByAccountIdAndDomainAndTestStepPersistentId(Long userId,
			String domain, Long testStepPersistentId) {
		return userTestStepReportRepository.findIndependantByAccountIdAndDomainAndTestStepPersistentId(userId,domain,testStepPersistentId);
	}
	
	

}
