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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.domain.util.Views;
import gov.nist.hit.core.repo.TestCaseGroupRepository;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestCaseService;
import gov.nist.hit.core.service.TestPlanService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.DomainException;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 */
@RequestMapping("/cb")
@RestController
@Api(value = "Context based Testing", tags = "Context-based Testing", position = 2)
public class ContextBasedController {

	static final Logger logger = LoggerFactory.getLogger(ContextBasedController.class);

	@Autowired
	private TestPlanService testPlanService;

	@Autowired
	private TestCaseService testCaseService;

	@Autowired
	private TestStepService testStepService;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	protected TestCaseGroupRepository testCaseGroupRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private Streamer streamer;

	@ApiOperation(value = "Find all context-based test cases list by scope", nickname = "getTestPlansByScope")
	@RequestMapping(value = "/testplans", method = RequestMethod.GET, produces = "application/json")
	public void getTestPlansByScope(
			@ApiParam(value = "the scope of the test plans", required = false) @RequestParam(required = false) TestScope scope,
			@ApiParam(value = "the domain of the test plans", required = true) @RequestParam(required = true) String domain,
			HttpServletRequest request, HttpServletResponse response) throws IOException, NoUserFoundException {
		logger.info("Fetching all testplans of type=" + scope + "...");
		List<TestPlan> results = null;
		scope = scope == null ? TestScope.GLOBAL : scope;
		if (TestScope.USER.equals(scope)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = accountService.findOne(userId);
				if(account != null) {
					String email = account.getEmail();
					if (userService.isAdminByEmail(email) || userService.isAdmin(account.getUsername())) {
						results = testPlanService.findShortAllByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
					}else
					results = testPlanService.findAllShortByStageAndUsernameAndScopeAndDomain(TestingStage.CB,
							account.getUsername(), scope, domain);
				}
			}
		} else {
			results = testPlanService.findShortAllByStageAndScopeAndDomain(TestingStage.CB, scope, domain);
		}
		streamer.stream(response.getOutputStream(), results);
	}
	
	@ApiOperation(value = "Find a context-based test plan by its id", nickname = "getOneTestPlanById")
	@RequestMapping(value = "/testplans/{testPlanId}", method = RequestMethod.GET, produces = "application/json")
	public TestPlan testPlan(
			@ApiParam(value = "the id of the test plan", required = true) @PathVariable final Long testPlanId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("Fetching  test case...");	
		TestPlan testPlan = testPlanService.findOne(testPlanId);
		Long userId = SessionContext.getCurrentUserId(request.getSession(false));
		recordTestPlan(testPlan, userId);
		return testPlan;
	}
	
	
	@RequestMapping(value = "/testplans/{testPlanId}/updateDate", method = RequestMethod.GET, produces = "application/json")
	public Date updateDate(HttpServletRequest request, @PathVariable("testPlanId") Long testPlanId, Authentication authentication)
			throws DomainException {
		try {
			Date date = testPlanService.getUpdateDate(testPlanId);
			return date;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "testplans/{testPlanId}/details", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> details(HttpServletResponse response,
			@ApiParam(value = "the id of the test plan", required = true) @PathVariable final Long testPlanId)
			throws IOException {
		logger.info("Fetching artifacts of testplan with id=" + testPlanId);
		TestPlan testPlan = testPlanService.findOne(testPlanId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("testStory", testPlan.getTestStory());
		result.put("supplements", testPlan.getSupplements());
		result.put("updateDate", testPlan.getUpdateDate());
		return result;
	}
	

	private void recordTestPlan(TestPlan testPlan, Long userId) {
		if (testPlan != null && userId != null) {
			accountService.recordLastTestPlan(userId, testPlan.getPersistentId());
		}
	}

	// @ApiOperation(value = "Get all context-based test cases list", nickname =
	// "getAllContextBasedTestCases")
	// @RequestMapping(value = "/testcases", method = RequestMethod.GET,
	// produces = "application/json")
	// public JsonView<List<TestPlan>> testCases() {
	// logger.info("Fetching all testCases...");
	// List<TestPlan> testPlans =
	// testPlanService.findAllByStage(TestingStage.CB);
	// return JsonView.with(testPlans).onClass(TestPlan.class,
	// match().exclude("testCases").exclude("testCaseGroups"));
	// }

	@ApiOperation(value = "Get a context-based test case by id", nickname = "getOneContextBasedTestCaseById")
	@RequestMapping(value = "/testcases/{testCaseId}", method = RequestMethod.GET, produces = "application/json")
	public TestCase testCase(
			@ApiParam(value = "the id of the test case", required = true) @PathVariable final Long testCaseId) {
		logger.info("Fetching  test case...");
		TestCase testCase = testCaseService.findOne(testCaseId);
		return testCase;
	}
	
	/**
	 * 
	 * @param testCaseId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/testcases/{testCaseId}/details", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> tcdetails(HttpServletResponse response, @PathVariable("testCaseId") final Long testCaseId)
			throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		logger.info("Fetching testcase " + testCaseId + " artifacts ");
		TestCase testCase = testCaseService.findOne(testCaseId);
		result.put("testStory", testCase.getTestStory());
		result.put("jurorDocument", testCase.getJurorDocument());
		result.put("supplements", testCase.getSupplements());
		result.put("updateDate", testCase.getUpdateDate());
		return result;
	}
	
	@RequestMapping(value = "/testcases/{testCaseId}/updateDate", method = RequestMethod.GET, produces = "application/json")
	public Date tcUpdateDate(HttpServletRequest request, @PathVariable("testCaseId") Long testCaseId, Authentication authentication)
			throws DomainException {
		try {
			Date date = testCaseService.getUpdateDate(testCaseId);
			return date;
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}

	@ApiOperation(value = "Get a context-based test step by id", nickname = "getOneContextBasedTestStepById", hidden = true)
	@RequestMapping(value = "/teststeps/{testStepId}", method = RequestMethod.GET, produces = "application/json")
	public TestStep testStep(
			@ApiParam(value = "the id of the test step", required = true) @PathVariable final Long testStepId) {
		logger.info("Fetching  test step...");
		TestStep testStep = testStepService.findOne(testStepId);
		return testStep;
	}
	
	@RequestMapping(value = "/teststeps/{testStepId}/details", method = RequestMethod.GET)
	public Map<String, Object> tsdetails(HttpServletResponse response,
			@ApiParam(value = "the id of the test step", required = true) @PathVariable final Long testStepId)
			throws IOException {
		logger.info("Fetching artifacts of teststep with id=" + testStepId);
		TestStep testStep = testStepService.findOne(testStepId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("jurorDocument", testStep.getJurorDocument());
		result.put("messageContent", testStep.getMessageContent());
		result.put("testDataSpecification", testStep.getTestDataSpecification());
		result.put("testStory", testStep.getTestStory());
		result.put("supplements", testStep.getSupplements());
		result.put("updateDate", testStep.getUpdateDate());
		return result;
	}
	
	@RequestMapping(value = "/teststeps/{testStepId}/updateDate", method = RequestMethod.GET, produces = "application/json")
	public Date tsupdateDate(HttpServletRequest request, @PathVariable("testStepId") Long testStepId, Authentication authentication)
			throws DomainException {
		try {
			Date date = testStepService.getUpdateDate(testStepId);
			return date;
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}
	
	@RequestMapping(value = "/testcasegroups/{testCaseGroupId}/details", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> tcsdetails(HttpServletResponse response,
			@ApiParam(value = "the id of the test case group", required = true) @PathVariable final Long testCaseGroupId)
			throws IOException {
		logger.info("Fetching artifacts of test case group with id=" + testCaseGroupId);
		TestCaseGroup testCaseGroup = testCaseGroupRepository.findOne(testCaseGroupId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("testStory", testCaseGroup.getTestStory());
		result.put("supplements", testCaseGroup.getSupplements());
		result.put("updateDate", testCaseGroup.getUpdateDate());
		return result;
	}
	
	@RequestMapping(value = "/testcasegroups/{testCaseGroupId}/updateDate", method = RequestMethod.GET, produces = "application/json")
	public Date tcgUpdateDate(HttpServletRequest request, @PathVariable("testCaseGroupId") Long testCaseGroupId, Authentication authentication)
			throws DomainException {
		try {
			Date date = testCaseGroupRepository.getUpdateDate(testCaseGroupId);
			return date;
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}
	
	

}
