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

import java.io.File;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.TestScope;
import gov.nist.hit.core.domain.TestingStage;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.CFTestPlanService;
import gov.nist.hit.core.service.CFTestStepService;
import gov.nist.hit.core.service.DomainService;
import gov.nist.hit.core.service.ResourceLoader;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.DomainException;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/cf")
@RestController
@Api(value = "Context-free Testing", tags = "Context-free Testing", position = 1)
public class ContextFreeController {

	static final Logger logger = LoggerFactory.getLogger(ContextFreeController.class);

    public static final String CF_UPLOAD_DIR = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + "/cf";

	@Autowired
	private CFTestStepService testStepService;

	@Autowired
	private CFTestPlanService testPlanService;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DomainService domainService;

	@Autowired
	@Qualifier("resourceLoader")
	private ResourceLoader resouceLoader;

	@Autowired
	private Streamer streamer;

	@Value("${server.email}")
	private String SERVER_EMAIL;

	@Value("${mail.tool}")
	private String TOOL_NAME;
	

	

	@ApiOperation(value = "Find all context-free test plans by scope", nickname = "getTestPlansByScope")
	// @Cacheable(value = "HitCache", key = "'cb-testplans'")
	@RequestMapping(value = "/testplans", method = RequestMethod.GET, produces = "application/json")
	public List<CFTestPlan>  getTestPlansByScope(
			@ApiParam(value = "the scope of the test plans", required = false) @RequestParam(required = false) TestScope scope,
			@ApiParam(value = "the domain of the test plans", required = true) @RequestParam(required = true) String domain,
			HttpServletRequest request, HttpServletResponse response) throws IOException, NoUserFoundException {
		logger.info("Fetching context-free testplans of scope=" + scope + "...");
		List<CFTestPlan> results = null;
		scope = scope == null ? TestScope.GLOBAL : scope;
		if (TestScope.USER.equals(scope)) {
			Long userId = SessionContext.getCurrentUserId(request.getSession(false));
			if (userId != null) {
				Account account = accountService.findOne(userId);
				if (account != null) {
					String email = account.getEmail();
					if (userService.isAdminByEmail(email) || userService.isAdmin(account.getUsername())) {
						results = testPlanService.findShortAllByScopeAndDomain(scope, domain);
					} else {
						results = testPlanService.findShortAllByScopeAndUsernameAndDomain(scope, account.getUsername(),
								domain);
					}
				}

			}
		} else {
			results = testPlanService.findShortAllByStageAndScopeAndDomain(TestingStage.CF, scope, domain);
		}
		return results;
	}

	@ApiOperation(value = "Find a context-free test plan by its id", nickname = "getOneTestPlanById")
	@RequestMapping(value = "/testplans/{testPlanId}", method = RequestMethod.GET, produces = "application/json")
	public CFTestPlan testPlan(
			@ApiParam(value = "the id of the test plan", required = true) @PathVariable final Long testPlanId,
			HttpServletRequest request, HttpServletResponse response,Authentication auth) throws Exception {
		logger.info("Fetching  test case...");
		
		
		TestScope ts = testPlanService.getScope(testPlanId);
		if (ts!= null && ts.equals(TestScope.USER)) {
			String domain = testPlanService.getDomain(testPlanId);
			domainService.hasPermission(domain, auth);
		}		
		
		CFTestPlan testPlan = testPlanService.findOne(testPlanId);
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
	
	@RequestMapping(value = "/{testPlanId}/details", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> details(HttpServletResponse response,
			@ApiParam(value = "the id of the test plan", required = true) @PathVariable final Long testPlanId)
			throws IOException {
		logger.info("Fetching artifacts of testplan with id=" + testPlanId);
		CFTestPlan testPlan = testPlanService.findOne(testPlanId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("testStory", testPlan.getTestStory());
		result.put("supplements", testPlan.getSupplements());
		result.put("updateDate", testPlan.getUpdateDate());
		return result;
	}

	private void recordTestPlan(CFTestPlan testPlan, Long userId) {
		if (testPlan != null && userId != null) {
			accountService.recordLastCFTestPlan(userId, testPlan.getPersistentId());
		}
	}

	public CFTestStepService getTestStepService() {
		return testStepService;
	}

	public void setTestStepService(CFTestStepService testStepService) {
		this.testStepService = testStepService;
	}

	public CFTestPlanService getTestPlanService() {
		return testPlanService;
	}

	public void setTestPlanService(CFTestPlanService testPlanService) {
		this.testPlanService = testPlanService;
	}

	public AccountService getUserService() {
		return accountService;
	}

	public void setUserService(AccountService accountService) {
		this.accountService = accountService;
	}

}
