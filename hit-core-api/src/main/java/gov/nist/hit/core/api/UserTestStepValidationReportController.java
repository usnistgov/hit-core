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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import gov.nist.auth.hit.core.domain.UserTestStepReport;
import gov.nist.auth.hit.core.domain.ValidationLog;
import gov.nist.auth.hit.core.service.UserTestStepReportService;
import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.domain.TestResult;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.TestStepValidationReportRequest;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.UserTestStepReportRequest;
import gov.nist.hit.core.domain.ResponseMessage.Type;
import gov.nist.hit.core.domain.util.Views;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import gov.nist.hit.core.service.exception.TestStepException;
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
@RequestMapping("/userTSReport")
@Api(value = "User Test Step validation report api", tags = "Test Step Validation Report", position = 5)
public class UserTestStepValidationReportController {

	static final Logger logger = LoggerFactory.getLogger(UserTestStepValidationReportController.class);

	@Autowired
	private TestStepValidationReportService validationReportService;

	@Autowired
	private TestStepService testStepService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserTestStepReportService userTestStepReportService;

	@Autowired
	private Streamer streamer;
	
	@Autowired
	private UserService userService;

	

	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "downloadPersistentUserTestStepReport", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8")
	public void downloadPersistentUserTestStepReport(
			@ApiParam(value = "the targeted format (html,pdf etc...)", required = true) @RequestParam("format") String format,
			@ApiParam(value = "the account id of the user", required = true) @RequestParam("accountId") final Long accountId,
			@ApiParam(value = "the id of the test step", required = true) @PathVariable("testStepId") Long testStepId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("Downloading validation report  in " + format);
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId == null || (accountService.findOne(userId) == null))
				throw new MessageValidationException("Invalid user credentials");
			if (format == null)
				throw new ValidationReportException("No format specified");
			TestStep testStep = testStepService.findOne(testStepId);
			if (testStep == null) {
				throw new TestStepException(testStepId);
			}
			UserTestStepReport userTestStepReport = userTestStepReportService.findOneByAccountIdAndTestStepPersistentId(accountId,testStep.getPersistentId());
			if (userTestStepReport == null) {
				logger.error("No testStep Report for account " + accountId + " and testStep " + testStepId);
				throw new ValidationReportException(
						"No testStepReport for account " + accountId + " and testStep " + testStepId);
			}
			if (userTestStepReport.getXml() == null) {
				throw new ValidationReportException("No validation report available for this test step");
			}
			String title = testStep.getName();
			String ext = format.toLowerCase();
			InputStream io = null;
			if ("HTML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(userTestStepReport.getHtml(), "UTF-8");
				response.setContentType("text/html");
			} else if ("XML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(userTestStepReport.getXml(), "UTF-8");
				response.setContentType("application/xml");
			} else if ("PDF".equalsIgnoreCase(format)) {
				io = validationReportService.generatePdf(userTestStepReport.getXml());
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

	
	@ApiOperation(value = "", hidden = true)
	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "{reportId}/download/{format}", method = RequestMethod.GET)
	public void download(@PathVariable("reportId") Long reportId,@PathVariable("format") String format,Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("Downloading validation report  in " + format);
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId == null || (accountService.findOne(userId) == null))
				throw new MessageValidationException("Invalid user credentials");
						
			UserTestStepReport userTestStepReport = userTestStepReportService.findOne(reportId);
			if (userTestStepReport == null) {
				logger.error("No testStep Report for account " + userId + " and reportId " + reportId);
				throw new ValidationReportException(
						"No testStepReport for account " + userId + " and reportId " + reportId);
			}
			
			if (userTestStepReport.getXml() == null) {
				throw new ValidationReportException("No validation report available for this test step");
			}
			String title = userTestStepReport.getName();
			String ext = format.toLowerCase();
			InputStream io = null;
			if ("HTML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(userTestStepReport.getHtml(), "UTF-8");
				response.setContentType("text/html");
			} else if ("XML".equalsIgnoreCase(format)) {
				io = IOUtils.toInputStream(userTestStepReport.getXml(), "UTF-8");
				response.setContentType("application/xml");
			} else if ("PDF".equalsIgnoreCase(format)) {
				io = validationReportService.generatePdf(userTestStepReport.getXml());
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
	
	
	
	
	@ApiOperation(value = "", hidden = true)
	@RequestMapping(value = "/savePersistentUserTestStepReport", method = RequestMethod.POST, produces = "application/json")
	public UserTestStepReport savePersistentUserTestStepReport(@RequestBody UserTestStepReportRequest command,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("Saving persistent test step report");
		try {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			Account user = accountService.findOne(userId);
			if (user == null) {
				logger.error("Account " + userId + " not found");
				throw new UserNotFoundException("Account " + userId + " not found");
			}
//			Long testStepId = command.getTestStepId();
			Long testStepvalidationReportId = command.getTestStepValidationReportId();
			
			TestStepValidationReport report = validationReportService.findOne(testStepvalidationReportId);
			if (report == null) {
				throw new TestStepException("Can't find test step validation report with "+testStepvalidationReportId );
			}
			TestStep testStep = testStepService.findOne(report.getTestStepId());
			if (testStep == null) {
				throw new TestStepException(report.getTestStepId());
			}
			
			
			String xmlReport = generateXml(report, testStep.getStage(), testStep);
			String htmlReport = generateHtml(xmlReport);
			
		
			UserTestStepReport userTestStepReport = new UserTestStepReport(testStep.getName(),testStep.getDomain(),testStep.getStage(),report.getResult(),xmlReport, htmlReport, report.getJson(),
					testStep.getVersion(), user.getId(), testStep.getPersistentId(), report.getComments());
			
			
			userTestStepReportService.save(userTestStepReport);
			return userTestStepReport;
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@JsonView(Views.NoData.class)
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all user reports", nickname = "getAll")
	@RequestMapping(value = "/domain/{domain}", method = RequestMethod.GET, produces = "application/json")
	public List<UserTestStepReport> getAll(@PathVariable("domain") String domain, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all validation reports...");
		checkPermission(authentication);
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		Account user = null;
		if (userId == null || ((user = accountService.findOne(userId)) == null))
			throw new MessageValidationException("Invalid user credentials");
		return userTestStepReportService.findAllByAccountIdAndDomain(userId,domain);
	}
	
	@JsonView(Views.NoData.class)
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all user reports for a test step", nickname = "getAll")
	@RequestMapping(value = "/domain/{domain}/testStep/{testStepPersistentId}", method = RequestMethod.GET, produces = "application/json")
	public List<UserTestStepReport> getAllOfTestStep(
			@PathVariable("domain") String domain,
			@PathVariable("testStepPersistentId") Long testStepPersistentId,
			@RequestParam(value="onlyIndependant",defaultValue = "false") final Boolean onlyIndependant,
			Authentication authentication,HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all validation logs...");
		checkPermission(authentication);
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		Account user = null;
		if (userId == null || ((user = accountService.findOne(userId)) == null))
			throw new MessageValidationException("Invalid user credentials");
		
		if (onlyIndependant) {
			return userTestStepReportService.findIndependantByAccountIdAndDomainAndTestStepPersistentId(userId,domain,testStepPersistentId);
		}else {
			return userTestStepReportService.findAllByAccountIdAndDomainAndTestStepPersistentId(userId,domain,testStepPersistentId);
		}
	}
		
	

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "delete report", nickname = "delete report by id")
	@RequestMapping(value = "/{id}/deleteReport", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage deleteReport(Authentication authentication, HttpServletRequest request,
			@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
		logger.info("deleting user report with id=" + id + "...");
		checkPermission(authentication);
		userTestStepReportService.delete(id);
		return new ResponseMessage(Type.success, "User Test Step report " + id + " deleted successfully", id + "", true);
	}
	
	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get report", nickname = "get report by id")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public UserTestStepReport getReport(Authentication authentication, HttpServletRequest request,
			@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
		if (id == null) return null;
		logger.info("retrieving user report with id=" + id + "...");
		checkPermission(authentication);
		UserTestStepReport report = userTestStepReportService.findOne(id);
		return report;
	}
	

	private TestStepValidationReport findReport(Long testReportId, Long testStepId, Long userId) {
		if (testReportId != null) {
			return validationReportService.findOne(testReportId);
		} else {
			List<TestStepValidationReport> reports = validationReportService.findAllByTestStepAndUser(testStepId,
					userId);
			if (reports != null && !reports.isEmpty()) {
				if (reports.size() > 1) {
					Collections.sort(reports, new Comparator<TestStepValidationReport>() {
						@Override
						public int compare(TestStepValidationReport o1, TestStepValidationReport o2) {
							return o2.getDateUpdated().compareTo(o1.getDateUpdated());
						}
					});
					for (int i = 1; i < reports.size(); i++) {
						validationReportService.delete(reports.get(i).getId());
					}
				}
				return reports.get(0);
			}
		}

		return null;
	}

	private String generateXml(TestStepValidationReport report, TestingStage stage, TestStep testStep) {
		if (TestingStage.CF.equals(stage)) {
			return validationReportService.updateXmlTestValidationReportElement(report);
		} else {
			return validationReportService.generateXmlTestStepValidationReport(report.getXml(), report, testStep);
		}
	}

	

	public InputStream generatePdf(TestStepValidationReport report) {
		try {
			InputStream io = null;
			String xmlReport = report.getXml();
			io = validationReportService.generatePdf(xmlReport);
			return io;
		} catch (ValidationReportException e) {
			throw new ValidationReportException("Failed to generate the report pdf");
		} catch (Exception e) {
			throw new ValidationReportException("Failed to generate the report pdf");
		}
	}

	private String generateHtml(String xmlReport) {
		return validationReportService.generateHtml(xmlReport);
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
