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
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.repo.TestCaseGroupRepository;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.exception.DomainException;
import gov.nist.hit.core.service.exception.TestCaseGroupException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */

@RestController
@RequestMapping("/testcasegroups")
public class TestCaseGroupController {

	static final Logger logger = LoggerFactory.getLogger(TestCaseGroupController.class);

	@Autowired
	protected TestCaseGroupRepository testCaseGroupRepository;

	@Autowired
	private Streamer streamer;

	@ApiOperation(value = "Get a test case group by its id", nickname = "getTestCaseGroupById")
	@RequestMapping(value = "/{testCaseGroupId}")
	public void testCaseGroup(HttpServletResponse response,
			@ApiParam(value = "the id of the test case group", required = true) @PathVariable final Long testCaseGroupId)
			throws IOException {
		logger.info("Fetching test case group with id=" + testCaseGroupId);
		TestCaseGroup testCaseGroup = findTestCaseGroup(testCaseGroupId);
		streamer.stream(response.getOutputStream(), testCaseGroup);
	}

	private TestCaseGroup findTestCaseGroup(Long testCaseGroupId) {
		TestCaseGroup testCaseGroup = testCaseGroupRepository.findOne(testCaseGroupId);
		if (testCaseGroup == null) {
			throw new TestCaseGroupException(testCaseGroupId);
		}
		return testCaseGroup;
	}

	@RequestMapping(value = "/{testCaseGroupId}/details", method = RequestMethod.GET, produces = "application/json")
	public void details(HttpServletResponse response,
			@ApiParam(value = "the id of the test case group", required = true) @PathVariable final Long testCaseGroupId)
			throws IOException {
		logger.info("Fetching artifacts of test case group with id=" + testCaseGroupId);
		TestCaseGroup testCaseGroup = findTestCaseGroup(testCaseGroupId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("testStory", testCaseGroup.getTestStory());
		result.put("supplements", testCaseGroup.getSupplements());
		result.put("updateDate", testCaseGroup.getUpdateDate());
		streamer.stream(response.getOutputStream(), result);
	}
	
	@RequestMapping(value = "/{testCaseGroupId}/updateDate", method = RequestMethod.GET, produces = "application/json")
	public Date updateDate(HttpServletRequest request, @PathVariable("testStepId") Long testCaseGroupId, Authentication authentication)
			throws DomainException {
		try {
			Date date = testCaseGroupRepository.getUpdateDate(testCaseGroupId);
			return date;
		} catch (Exception e) {
			throw new DomainException(e);
		}
	}

}
