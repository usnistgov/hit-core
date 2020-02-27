package gov.nist.auth.hit.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.UserTestCaseReport;
import gov.nist.auth.hit.core.domain.UserTestStepReport;
import gov.nist.auth.hit.core.repo.UserTestCaseReportRepository;
import gov.nist.auth.hit.core.service.UserTestCaseReportService;

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
@Service(value = "userTestCaseReportService")
public class UserTestCaseReportServiceImpl implements UserTestCaseReportService {

    @Autowired
    UserTestCaseReportRepository userTestCaseReportRepository;

    @Override
    public UserTestCaseReport findOneByAccountAndTestCaseId(Long accountId,Long testCaseId) {
        return userTestCaseReportRepository.findOneByAccountIdAndTestCaseId(accountId,testCaseId);
    }

    @Override
    public UserTestCaseReport save(UserTestCaseReport userTestCaseReport) {
        return userTestCaseReportRepository.saveAndFlush(userTestCaseReport);
    }
    
    @Override
    public UserTestCaseReport findOne(Long userTestCaseReportId) {
      return userTestCaseReportRepository.findOne(userTestCaseReportId);
    }

    @Override
    public void delete(UserTestCaseReport userTestCaseReport) {
        userTestCaseReportRepository.delete(userTestCaseReport);
    }
    
    @Override
	public void delete(Long id) {
    	userTestCaseReportRepository.delete(id);
	}
    
    @Override
	public List<UserTestCaseReport> findAllByAccountIdAndDomain(Long accountId,String domain) {
		return userTestCaseReportRepository.findAllByAccountIdAndDomain(accountId,domain);
	}

	@Override
	public List<UserTestCaseReport> findAllByAccountIdAndDomainAndTestCasePersistentId(Long userId, String domain,
			Long persistentId) {
		return userTestCaseReportRepository.findAllByAccountIdAndDomainAndTestCasePersistentId(userId,domain,persistentId);
	}
    
    
}
