package gov.nist.hit.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.ValidationLog;
import gov.nist.auth.hit.core.repo.ValidationLogRepository;
import gov.nist.healthcare.unified.exceptions.ConversionException;
import gov.nist.healthcare.unified.exceptions.NotFoundException;
import gov.nist.healthcare.unified.model.Classification;
import gov.nist.healthcare.unified.model.Collection;
import gov.nist.healthcare.unified.model.Detections;
import gov.nist.healthcare.unified.model.EnhancedReport;
import gov.nist.healthcare.unified.model.Section;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserIdService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.ValidationLogService;
import gov.nist.hit.core.service.util.DateUtil;
import gov.nist.hit.core.service.util.ValidationLogUtil;

@Service
public class ValidationLogServiceImpl implements ValidationLogService {

	static final Logger logger = LoggerFactory.getLogger(ValidationLogServiceImpl.class);

	@Autowired
	private ValidationLogRepository validationLogRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserIdService userIdService;

	@Autowired
	protected TestStepService testStepService;

	@Override
	public ValidationLog findOne(Long id) {
		// TODO Auto-generated method stub
		return validationLogRepository.findOne(id);
	}

	@Override
	public ValidationLog save(ValidationLog log) {
		// TODO Auto-generated method stub
		return validationLogRepository.saveAndFlush(log);
	}

	@Override
	public List<ValidationLog> findByUserId(Long userId, String domain) {
		// TODO Auto-generated method stub
		return validationLogRepository.findByUserId(userId, domain);
	}

	@Override
	public List<ValidationLog> findByUserIdAndStage(Long userId, String testingStage, String domain) {
		// TODO Auto-generated method stub
		return validationLogRepository.findByUserIdAndStage(userId, testingStage, domain);
	}

	@Override
	public List<ValidationLog> findByTestStepId(Long testStepId, String domain) {
		// TODO Auto-generated method stub
		return validationLogRepository.findByTestStepId(testStepId, domain);
	}

	@Override
	public ValidationLog generateAndSave(Long userId, TestContext testContext, EnhancedReport report) {
		ValidationLog log = new ValidationLog();
		log.setUserId(userId);
		log.setTestStepId(testContext.getTestStep().getPersistentId());
		log.setDate(new Date());
		log.setErrorCountInSegment(new HashMap<>());
		log.setMessage(report.getMessage());
		TestStep step = testContext.getTestStep();
		if (step != null) {
			String userfullName = null;
			Account account = null;
			if (log.getUserId() != null) {
				account = accountService.findOne(log.getUserId());
			}
			userfullName = account != null && account.getFullName() != null ? account.getFullName()
					: "Guest-" + log.getUserId();
			log.setUserFullname(userfullName);
			log.setCompanyName(account != null ? account.getEmployer() : "NA");
			log.setTestStepName(step.getName());
			log.setTestingStage(
					(step.getStage() != null && step.getStage().name() != null ? step.getStage().name() : ""));
			log.setDomain(step.getDomain());
		}

		Detections detections = report.getDetections();
		// Loop on the classifications (Affirmative, Warning or Error)
		int totalErrorCount = 0;
		int totalWarningCount = 0;
		for (Classification classification : detections.classes()) {
			if (classification.getName().equals("Affirmative")) {
				// No need to display any log here
			} else if (classification.getName().equals("Warning")) {
				// Get the warning count
				for (String key : classification.keys()) {
					Collection collection = null;
					try {
						collection = classification.getArray(key);
						if (collection != null && collection.size() > 0) {
							for (int i = 0; i < collection.size(); i++) {
								totalWarningCount++;
							}
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					} catch (ConversionException e) {
						e.printStackTrace();
					}
				}
			} else if (classification.getName().equals("Error")) {
				// Loop on the errors
				for (String key : classification.keys()) {
					Collection collection = null;
					try {
						collection = classification.getArray(key);
						if (collection != null && collection.size() > 0) {
							for (int i = 0; i < collection.size(); i++) {
								totalErrorCount++;
								Section section = collection.getObject(i);
								// Identify the path of the error
								String path = section.getString("path");
								if (path != null && !"".equals(path)) {
									path = path.split("\\[")[0];
									int segmentErrorCount = 1;
									// If there was already at least 1 error
									// for this segment, then increment its
									// error count.
									if (log.getErrorCountInSegment().containsKey(path)) {
										segmentErrorCount = log.getErrorCountInSegment().get(path) + 1;
									}
									// Add or update the segment's error
									// count
									log.getErrorCountInSegment().put(path, segmentErrorCount);
								}
							}
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					} catch (ConversionException e) {
						e.printStackTrace();
					}
				}
			}
			// Get the error count
			log.setErrorCount(totalErrorCount);
			// If there is more than 0 errors, then the validation failed
			if (totalErrorCount > 0) {
				log.setValidationResult(false);
			}
			log.setWarningCount(totalWarningCount);
		}
		// Parse the test context
		if (testContext != null) {
			log.setFormat((testContext.getFormat() != null ? testContext.getFormat() : ""));
			log.setMessageId((testContext.getType() != null ? testContext.getType() : ""));
		}
		String validationLog = ValidationLogUtil.toString(log);
		logger.info(validationLog.toString());
		return save(log);
	}

	@Override
	public List<ValidationLog> findAll(String domain) {
		// TODO Auto-generated method stub
		Date current = new Date();
		Date startDate = DateUtil.getFirstDateOfMonth(current);
		Date endDate = DateUtil.getLastDateOfMonth(current);
		return validationLogRepository.findAllBetweenDate(startDate, endDate, domain);
	}

	@Override
	public long countAll(String domain) {
		Date current = new Date();
		Date startDate = DateUtil.getFirstDateOfMonth(current);
		Date endDate = DateUtil.getLastDateOfMonth(current);
		return validationLogRepository.countBetweenDate(startDate, endDate, domain);
	}

	@Override
	public void delete(Long id) {
		validationLogRepository.delete(id);
	}

	private Sort sortByDateDsc() {
		return new Sort(Sort.Direction.DESC, "date");
	}

}
