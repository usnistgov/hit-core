/**
l * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */

package gov.nist.hit.core.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import antlr.Utils;
import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.Report;
import gov.nist.auth.hit.core.domain.UserTestCaseReport;
import gov.nist.auth.hit.core.domain.UserTestStepReport;
import gov.nist.auth.hit.core.service.UserTestCaseReportService;
import gov.nist.auth.hit.core.service.UserTestStepReportService;
import gov.nist.hit.core.domain.ReportType;
import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.ResponseMessage.Type;
import gov.nist.hit.core.domain.util.Views;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestCaseValidationReportService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/reports")
@Api(value = "Report api", tags = "Validation Reports", position = 6)
public class ReportController {

	static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private UserTestStepReportService userTestStepReportService;

	@Autowired
	private UserTestCaseReportService userTestCaseReportService;


	
	
	
	@Autowired
	private UserService userService;
	
	

	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all reports", nickname = "getAll")
	@RequestMapping(value = "/{domain}", method = RequestMethod.GET, produces = "application/json")
	public List<Report> getAll(@PathVariable("domain") String domain, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all validation logs...");
		checkPermission(authentication);
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		Account user = null;
		if (userId == null || ((user = accountService.findOne(userId)) == null))
			throw new MessageValidationException("Invalid user credentials");
		
		
		List<Report> reports = new ArrayList<Report>();
		List<UserTestCaseReport> tcreports = userTestCaseReportService.findAllByAccountIdAndDomain(userId,domain);		
		List<UserTestStepReport> tsreports = userTestStepReportService.findAllByAccountIdAndDomain(userId,domain);
		
		
		for (UserTestCaseReport tc : tcreports) {
			Report rtc = new Report(tc.getId(),tc.getName(),tc.getStage(),ReportType.TESTCASE, tc.getVersion(),tc.getResult(),tc.getAccountId(),tc.getTestCasePersistentId(),tc.getCreationDate(),tc.getDomain());
			for (UserTestStepReport ts : tc.getUserTestStepReports()) {
				Report rts = new Report(ts.getId(),ts.getName(),tc.getStage(),ReportType.TESTSTEP,ts.getVersion(),ts.getResult(),ts.getAccountId(),ts.getTestStepPersistentId(),ts.getCreationDate(),ts.getDomain());
				rtc.getReports().add(rts);
			}		
			reports.add(rtc);
		}
		//Tests steps  //cf
		for (UserTestStepReport ts : tsreports) {
			if (ts.getTestCaseReport() == null) {
				Report rts = new Report(ts.getId(),ts.getName(),ts.getStage(),ReportType.TESTSTEP,ts.getVersion(),ts.getResult(),ts.getAccountId(),ts.getTestStepPersistentId(),ts.getCreationDate(),ts.getDomain());
				reports.add(rts);
			}	
		}					
		return reports;
	}
	
	

	
	
	private void checkPermission(Authentication auth) throws Exception {
		String username = auth.getName();
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		Account account = accountService.findByTheAccountsUsername(username);
		if (account == null) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}		
	}

}
