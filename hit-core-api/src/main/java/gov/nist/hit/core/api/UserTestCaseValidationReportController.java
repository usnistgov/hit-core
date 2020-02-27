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

package gov.nist.hit.core.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.UserTestCaseReport;
import gov.nist.auth.hit.core.domain.UserTestStepReport;
import gov.nist.auth.hit.core.service.UserTestCaseReportService;
import gov.nist.auth.hit.core.service.UserTestStepReportService;
import gov.nist.hit.core.domain.PersistentReportRequest;
import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.domain.ResponseMessage.Type;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.UserTestCaseReportRequest;
import gov.nist.hit.core.domain.util.Views;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestCaseValidationReportService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.UserNotFoundException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RestController
@RequestMapping("/userTCReport")
@Api(value = "Test Case validation report api", tags = "Test Case Validation Report", position = 6)
public class UserTestCaseValidationReportController {

	static final Logger logger = LoggerFactory.getLogger(UserTestCaseValidationReportController.class);

	@Autowired
	private TestCaseValidationReportService testCaseValidationReportService;

	@Autowired
	private TestStepValidationReportService validationReportService;

	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserTestStepReportService userTestStepReportService;

	@Autowired
	private UserTestCaseReportService userTestCaseReportService;

	@Autowired
	private Streamer streamer;
	
	
	@Autowired
	private UserService userService;
	
	

	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "/savePersistentUserTestCaseReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTestCaseReport savePersistentUserTestCaseReport(@RequestBody UserTestCaseReportRequest command,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("Saving persistent test step report");
		try {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			Account user = accountService.findOne(userId);
			if (user == null) {
				logger.error("Account " + userId + " not found");
				throw new UserNotFoundException("Account " + userId + " not found");
			}
			Long testCaseId = command.getTestCaseId();
			TestCase testCase = testCaseService.findOne(testCaseId);
			if (testCase == null) {
				throw new TestCaseException(testCaseId);
			}		
		
			
			UserTestCaseReport userTestCaseReport = new UserTestCaseReport();
			
			userTestCaseReport.setName(testCase.getName());
			userTestCaseReport.setResult(TestResult.valueOf(command.getResult()));
			userTestCaseReport.setDomain(testCase.getDomain());
			userTestCaseReport.setAccountId(user.getId());
			userTestCaseReport.setTestCasePersistentId(testCase.getPersistentId());
			userTestCaseReport.setVersion(testCase.getVersion());
			String xml = testCaseValidationReportService.generateXml(testCase, userId, command.getResult(),	command.getComments(), command.getTestPlan(), command.getTestGroup());
			String html = testCaseValidationReportService.generateHtml(testCase, userId, command.getResult(), command.getComments(), command.getTestPlan(), command.getTestGroup());
			userTestCaseReport.setXml(xml);
			userTestCaseReport.setHtml(html);
			
			
			Set<UserTestStepReport> userTestStepReports = new HashSet<UserTestStepReport>();
			for (TestStep testStep : testCase.getTestSteps()) {
				UserTestStepReport userTestStepReport = generateUserTestStepReport(userId, testStep, testCase.getPersistentId());				
				if (userTestStepReport != null) {
					userTestStepReport.setTestCaseReport(userTestCaseReport);
					userTestStepReports.add(userTestStepReport);
				} else {
					logger.error("Unable to retrieve the report for testStep " + testStep.getId() + " and userId " + userId);
				}
			}
			userTestCaseReport.setUserTestStepReports(userTestStepReports);
			userTestCaseReportService.save(userTestCaseReport);
			logger.info("Persistent test case, report successfully saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//TODO success return
		return null;
	}

	private UserTestStepReport generateUserTestStepReport(Long userId, TestStep testStep, Long testCaseId) {
		TestStepValidationReport report = validationReportService.findOneByTestStepAndUser(testStep.getId(), userId);
		if (report == null) {
			logger.error("No report found for test step " + testStep.getId() + " and userId " + userId);
			return null;
		}
		UserTestStepReport userTestStepReport = new UserTestStepReport(testStep.getName(), testStep.getDomain(),testStep.getStage(), report.getResult(),
				report.getXml(), report.getHtml(), report.getJson(), testStep.getVersion(), userId,	testStep.getPersistentId(), report.getComments());
		return userTestStepReport;
	}
	
	
	

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "/downloadPersistentUserTestCaseReport", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8")
	public boolean downloadPersistentUserTestCaseReport(
			@ApiParam(value = "the id of the test case", required = true) @RequestParam("testCaseId") final Long testCaseId,
			@ApiParam(value = "the format of the report", required = true) @RequestParam("format") final String format,
			@ApiParam(value = "the account id of the user", required = true) @RequestParam("accountId") final Long accountId,
			HttpServletRequest request, HttpServletResponse response) throws ValidationReportException {
		try {
			logger.info("Downloading HTML for the persistent test case report");
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId == null || accountService.findOne(userId) == null)
				throw new ValidationReportException("Invalid user credentials");
			TestCase testCase = testCaseService.findOne(testCaseId);
			if (testCase == null)
				throw new TestCaseException(testCaseId);
			UserTestCaseReport userTestCaseReport = userTestCaseReportService.findOneByAccountAndTestCaseId(accountId,
					testCase.getPersistentId());
			String title = testCase.getName().replaceAll(" ", "-");
			InputStream io = null;
			if ("HTML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(testCaseValidationReportService.generateHtml(userTestCaseReport.getXml()),
						"UTF-8");
				response.setContentType("text/html");
			} else if ("XML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(userTestCaseReport.getXml(), "UTF-8");
				response.setContentType("application/xml");
			} else if ("PDF".equalsIgnoreCase(format)) {
				io = testCaseValidationReportService.generatePdf(userTestCaseReport.getXml());
				response.setContentType("application/pdf");
			} else {
				throw new ValidationReportException("Unsupported report format " + format);
			}
			response.setHeader("Content-disposition",
					"attachment;filename=" + title + "-ValidationReport." + format.toLowerCase());
			streamer.stream(response.getOutputStream(), io);
		} catch (Exception e) {
			throw new ValidationReportException("Failed to download the reports");
		}
		return true;
	}
	
	
	
	
	@ApiOperation(value = "", hidden = true)
	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "{reportId}/download/{format}", method = RequestMethod.GET)
	public void download(@PathVariable("reportId") Long reportId,@PathVariable("format") String format,Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("Downloading validation report  in " + format);
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId == null || (accountService.findOne(userId) == null))
				throw new MessageValidationException("Invalid user credentials");
						
			UserTestCaseReport userTestCaseReport = userTestCaseReportService.findOne(reportId);
			if (userTestCaseReport == null) {
				logger.error("No test case Report for account " + userId + " and reportId " + reportId);
				throw new ValidationReportException(
						"No testCaseReport for account " + userId + " and reportId " + reportId);
			}		
			if (userTestCaseReport.getXml() == null) {
				throw new ValidationReportException("No validation report available for this test case");
			}
			String title = userTestCaseReport.getName();
			String ext = format.toLowerCase();
			InputStream io = null;
			if ("HTML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(testCaseValidationReportService.generateHtml(userTestCaseReport.getXml()),
						"UTF-8");
				response.setContentType("text/html");
			} else if ("XML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(userTestCaseReport.getXml(), "UTF-8");
				response.setContentType("application/xml");
			} else if ("PDF".equalsIgnoreCase(format)) {
				io = testCaseValidationReportService.generatePdf(userTestCaseReport.getXml());
				response.setContentType("application/pdf");
			} else {
				throw new ValidationReportException("Unsupported report format " + format);
			}
			title = title.replaceAll(" ", "-");
			response.setHeader("Content-disposition", "attachment;filename=" + title + "-ValidationReport." + ext);
			streamer.stream(response.getOutputStream(), io);
		} catch (ValidationReportException | IOException e) {
			throw new ValidationReportException("Failed to generate the report");
		} catch (Exception e) {
			throw new ValidationReportException("Failed to generate the report");
		}
	}
	
	

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "/getPersistentUserTestCaseReportContent", method = RequestMethod.GET)
	public PersistentReportRequest getPersistentUserTestCaseReportContent(
			@ApiParam(value = "the id of the test case", required = true) @RequestParam("testCaseId") final Long testCaseId,
			HttpServletRequest request, HttpServletResponse response) throws ValidationReportException {
		PersistentReportRequest result = new PersistentReportRequest();
		try {
			logger.info("Downloading HTML for the persistent test case report");
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId == null || accountService.findOne(userId) == null) {
				logger.error("User not found");
				throw new ValidationReportException("Invalid user credentials");
			}
			Account user = accountService.findOne(userId);
			TestCase testCase = testCaseService.findOne(testCaseId);
			if (testCase == null) {
				logger.error("TestCase not found");
				throw new TestCaseException(testCaseId);
			}

			UserTestCaseReport userTestCaseReport = userTestCaseReportService
					.findOneByAccountAndTestCaseId(user.getId(), testCase.getPersistentId());
			if (userTestCaseReport != null) {
				result.setHtml(testCaseValidationReportService.generateHtml(userTestCaseReport.getXml()));
				result.setVersionChanged(!testCase.getVersion().equals(userTestCaseReport.getVersion()));
			}
		} catch (Exception e) {
			// The report does not exist. Do nothing.
			logger.info("No report found.");
		}
		return result;
	}

	
	
	@JsonView(Views.NoData.class)
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all reports", nickname = "getAll")
	@RequestMapping(value = "/domain/{domain}", method = RequestMethod.GET, produces = "application/json")
	public List<UserTestCaseReport> getAll(@PathVariable("domain") String domain, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all test case reports logs...");
		checkPermission(authentication);
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		Account user = null;
		if (userId == null || ((user = accountService.findOne(userId)) == null))
			throw new MessageValidationException("Invalid user credentials");
		return userTestCaseReportService.findAllByAccountIdAndDomain(userId,domain);
	}
	
	@JsonView(Views.NoData.class)
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all reports for a test case", nickname = "getAll")
	@RequestMapping(value = "/domain/{domain}/testCase/{testCasePersistentId}", method = RequestMethod.GET, produces = "application/json")
	public List<UserTestCaseReport> getAllOfTestCase(@PathVariable("domain") String domain,@PathVariable("testCasePersistentId") Long testCasePersistentId, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all test cases reports for a test case...");
		checkPermission(authentication);
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		Account user = null;
		if (userId == null || ((user = accountService.findOne(userId)) == null))
			throw new MessageValidationException("Invalid user credentials");
		return userTestCaseReportService.findAllByAccountIdAndDomainAndTestCasePersistentId(userId,domain,testCasePersistentId);
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get report", nickname = "get report by id")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public UserTestCaseReport getReport(Authentication authentication, HttpServletRequest request,
			@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
		if (id == null) return null;
		logger.info("retrieving user report with id=" + id + "...");
		checkPermission(authentication);
		UserTestCaseReport report = userTestCaseReportService.findOne(id);
		return report;
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete report", nickname = "delete report by id")
	@RequestMapping(value = "/{id}/deleteReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage deleteReport(Authentication authentication, HttpServletRequest request,
			@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
		logger.info("deleting user test case report with id=" + id + "...");
		checkPermission(authentication);
		userTestCaseReportService.delete(id);
		return new ResponseMessage(Type.success, "User Test Case report " + id + " deleted successfully", id + "", true);
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
