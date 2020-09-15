package gov.nist.hit.core.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.MessageModel;
import gov.nist.hit.core.domain.MessageParserCommand;
import gov.nist.hit.core.domain.MessageValidationCommand;
import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.MessageParser;
import gov.nist.hit.core.service.MessageValidator;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.TestStepValidationReportService;
import gov.nist.hit.core.service.ValidationReportConverter;
import gov.nist.hit.core.service.exception.MessageParserException;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Test Context", tags = "Test Context Controller")
public abstract class ValidationConfigurationController {

	Logger logger = LoggerFactory.getLogger(ValidationConfigurationController.class);


	public abstract ValidationReportConverter getValidatioReportConverter();

	

}
