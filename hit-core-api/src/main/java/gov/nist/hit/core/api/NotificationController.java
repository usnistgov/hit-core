package gov.nist.hit.core.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.hit.core.domain.Notification;
import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.domain.ResponseMessage.Type;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.NotificationService;
import gov.nist.hit.core.service.TestStepService;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.NoUserFoundException;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/notification")
@RestController
public class NotificationController {

	static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;

	@Autowired
	protected TestStepService testStepService;

	@Autowired
	private AccountService accountService;

	private void checkPermission(Authentication auth) throws Exception {
		String username = auth.getName();
		if (username == null)
			throw new NoUserFoundException("User could not be found");
		Account account = accountService.findByTheAccountsUsername(username);
		if (account == null) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
		if (!userService.hasGlobalAuthorities(username) && !userService.isAdminByEmail(account.getEmail())) {
			throw new NoUserFoundException("You do not have the permission to perform this task");
		}
	}

	@PreAuthorize("hasRole('tester')")
	@ApiOperation(value = "get all logs", nickname = "getAll")
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public List<Notification> getAll(Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Fetching all notifications...");
		checkPermission(authentication);
		return notificationService.findAll();
	}

	
	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage addNotification(Authentication authentication, HttpServletRequest request,
			@RequestBody Notification notification, HttpServletResponse response) {
		logger.info("adding notification");
		try {
			checkPermission(authentication);		
			Notification not = new Notification();
			not.setMessage(notification.getMessage());
			not.setDismissable(notification.isDismissable());
			not.setActive(notification.isActive());
			notificationService.save(not);
			ResponseMessage res = new ResponseMessage(Type.success, "Notification Log " + not.getId()+ " added successfully", not.getId() + "", true);
			res.setData(not);
			return res;
		} catch (Exception e) {
			return new ResponseMessage(Type.danger, "Notification Log could not be added.");
		}
	}
	
	@PreAuthorize("hasRole('tester')")
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage updateNotification(Authentication authentication, HttpServletRequest request,
			@RequestBody Notification notification, HttpServletResponse response) {
		logger.info("adding notification");
		try {
			checkPermission(authentication);		
			if (notification.getId() != null && notificationService.findOne(notification.getId())!= null) {
				notificationService.save(notification);
				return new ResponseMessage(Type.success, "Notification Log " + notification.getId()+ " added successfully", notification.getId() + "", true);
			}else {
				return new ResponseMessage(Type.danger, "Notification Log could not be updated.");
			}
						
		} catch (Exception e) {
			return new ResponseMessage(Type.danger, "Notification Log could not be updated.");
		}
	}

}
