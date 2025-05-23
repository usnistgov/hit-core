/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgment if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.hit.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import gov.nist.auth.hit.core.domain.Account;
import gov.nist.auth.hit.core.domain.util.UserUtil;
import gov.nist.hit.core.service.AccountService;
import gov.nist.hit.core.service.CustomJdbcUserDetailsManager;
import gov.nist.hit.core.service.UserService;
import gov.nist.hit.core.service.exception.NoUserFoundException;

/**
 * @author fdevaulx
 * 
 */
@PropertySources({
@PropertySource(value = { "classpath:app-config.properties" }),
@PropertySource(value = { "file:${propfile}" }, ignoreResourceNotFound= true)
})
@Service(value = "userService")
public class UserServiceImpl implements UserService {

	static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


	private final String DEFAULT_AUTHORITY = "user";
	private final String TESTER_AUTHORITY = "tester";
	private final String ADMIN_AUTHORITY = "admin";
	private final String DEPLOYER_AUTHORITY = "deployer";
	private final String SUPERVISOR_AUTHORITY = "supervisor";
	private final String PUBLISHER_AUTHORITY = "publisher";

	@Value("${admin.emails}")
	private String adminEmailsString;

	private Set<String> adminEmails;

	Set<String> PUBLIC_AUTHORITIES = new HashSet<>(Arrays.asList(PUBLISHER_AUTHORITY,SUPERVISOR_AUTHORITY, ADMIN_AUTHORITY));

	Set<String> ADMIN_AUTHORITIES = new HashSet<>(Arrays.asList(ADMIN_AUTHORITY));

	@Autowired
	private CustomJdbcUserDetailsManager jdbcUserDetailsManager;

	@Autowired
	@Qualifier(value = "shaPasswordEncoder")
	private ShaPasswordEncoder passwordEncoder;
	
	@Autowired
	private AccountService accountService;

	@PostConstruct
	public void init() {
		this.adminEmails = new HashSet<String>();
		if (this.adminEmailsString != null) {
			String[] emails = this.adminEmailsString.split(Pattern.quote(","));
			if (emails != null && emails.length > 0) {
				for (String e : emails) {
					this.adminEmails.add(e.trim());
				}
			}
		}
	}

	// @Autowired
	// private ReflectionSaltSource saltSource;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#userExists
	 * (java.lang.String)
	 */
	@Override
	public Boolean userExists(String username) {
		return jdbcUserDetailsManager.userExists(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#createUser
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void createUserWithDefaultAuthority(String username, String password) {
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority(DEFAULT_AUTHORITY));

		UserDetails userDetails = new User(username, passwordEncoder.encodePassword(password, username), true, true,
				true, true, roles);

		jdbcUserDetailsManager.createUser(userDetails);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#
	 * changePassword (java.lang.String, java.lang.String)
	 */
	@Override
	public void changePasswordForPrincipal(String oldPassword, String newPassword) throws BadCredentialsException {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User onRecordUser = this.retrieveUserByUsername(username);

		String oldEncodedPassword = onRecordUser.getPassword();
		String newEncodedPassword = passwordEncoder.encodePassword(newPassword, username);
		// logger.debug("[PASS] - old: " + oldEncodedPassword + " - new: " +
		// newEncodedPassword);
		if (oldEncodedPassword.equals(newEncodedPassword)) {
			throw new BadCredentialsException("New password must be different from previous password");
		}
		jdbcUserDetailsManager.changePassword(oldPassword, newEncodedPassword);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nist.itl.healthcare.ehrrandomizer.service.UserService#changePassword
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void changePasswordForUser(String oldPassword, String newPassword, String username)
			throws BadCredentialsException {
		String newEncodedPassword = passwordEncoder.encodePassword(newPassword, username);
		// logger.debug("[PASS] - old: " + oldPassword + " - new: " +
		// newEncodedPassword);
		if (oldPassword.equals(newEncodedPassword)) {
			throw new BadCredentialsException("New password must be different from previous password");
		}
		jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_CHANGE_PASSWORD_SQL,
				newEncodedPassword, username);
	}

	@Override
	public void changePasswordForUser(String newPassword, String username) throws BadCredentialsException {
		String newEncodedPassword = passwordEncoder.encodePassword(newPassword, username);
		// logger.debug("[PASS] - old: " + oldPassword + " - new: " +
		// newEncodedPassword);
		jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_CHANGE_PASSWORD_SQL,
				newEncodedPassword, username);
	}

	@Override
	public void changeAccountTypeForUser(String newAccountType, String username) throws BadCredentialsException {
		//clear all user authorities? 
		int del = jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_DELETE_USER_AUTHORITIES_SQL,
				username);
		int ins1 = jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,
				TESTER_AUTHORITY);
		int ins2 = jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,
				DEFAULT_AUTHORITY);
		if (!TESTER_AUTHORITY.equals(newAccountType)) {
			jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,
					newAccountType);
			//if we are setting an admin then it is also a deployer
			if (ADMIN_AUTHORITY.equals(newAccountType)) {
				jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL,
						username, DEPLOYER_AUTHORITY);
			}
		}
	}
	
	@Override
	public void changeAccountAuthoritiesForUser(List<String> authorities, String username)
			throws BadCredentialsException {
		//delete all
		int del = jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_DELETE_USER_AUTHORITIES_SQL,
				username);
		
		for (String auth : authorities) {
			switch(auth) {
			  case DEFAULT_AUTHORITY:
				  jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,DEFAULT_AUTHORITY);
			    break;
			  case TESTER_AUTHORITY:
				  jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,TESTER_AUTHORITY);
			    break;
			  case ADMIN_AUTHORITY:
				  jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,ADMIN_AUTHORITY);
			    break;
			  case DEPLOYER_AUTHORITY:
				  jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,DEPLOYER_AUTHORITY);
			    break;
			  case SUPERVISOR_AUTHORITY:
				  jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,SUPERVISOR_AUTHORITY);
			    break;
			  case PUBLISHER_AUTHORITY:
				  jdbcUserDetailsManager.getJdbcTemplate().update(jdbcUserDetailsManager.DEF_INSERT_AUTHORITY_SQL, username,PUBLISHER_AUTHORITY);
			    break;			  
			}

							
				
		}
		
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nist.itl.healthcare.ehrrandomizer.service.impl.UserService#deleteUser
	 * (java.lang.String)
	 */
	@Override
	public void deleteUser(String username) {
		jdbcUserDetailsManager.deleteUser(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nist.itl.healthcare.ehrrandomizer.service.UserService#disableUser
	 * (java.lang.String)
	 */
	@Override
	public void disableUser(String username) {
		User u = (User) jdbcUserDetailsManager.loadUserByUsername(username);
		// logger.debug("^^^^^^^^^^^^^^^^^^ u: "+u+" ^^^^^^^^^^^^^^^^");
		if (u != null) {
			// logger.debug("^^^^^^^^^^^^^^^^^^ u.isEnabled? "+u.isEnabled()+"
			// ^^^^^^^^^^^^^^^^");
			if (u.isEnabled()) {
				// logger.debug("^^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^");
				jdbcUserDetailsManager.getJdbcTemplate().update("update users set enabled = 0 where username = ?",
						u.getUsername());
			}
		}
	}

	@Override
	public void createUserWithAuthorities(String username, String password, Object authorities) throws Exception {
		String authoritiesString = (String) authorities;

		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.addAll(AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString));

		List<String> authList = new ArrayList<String>(Arrays.asList(UserUtil.AUTHORITY_LIST));
		for (GrantedAuthority ga : roles) {
			if (!authList.contains(ga.getAuthority())) {
				throw new Exception("Invalid authorization setting used");
			}
		}

		UserDetails userDetails = new User(username, passwordEncoder.encodePassword(password, username), true, true,
				true, true, roles);

		jdbcUserDetailsManager.createUser(userDetails);
	}

	@Override
	public List<String> findAllEnabledUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findAllDisabledUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User retrieveUserByUsername(String username) {
			return (User) jdbcUserDetailsManager.loadUserByUsername(username);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nist.itl.healthcare.ehrrandomizer.service.UserService#getCurrentUser
	 * ()
	 */
	@Override
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
			return null;
		}

		return (User) authentication.getPrincipal();
	}

	@Override
	public void enableUserCredentials(String username) {
		User u = (User) jdbcUserDetailsManager.loadUserByUsername(username);
		// logger.debug("^^^^^^^^^^^^^^^^^^ u: "+u+" ^^^^^^^^^^^^^^^^");
		if (u != null) {
			// logger.debug("^^^^^^^^^^^^^^^^^^ u.isEnabled? "+u.isEnabled()+"
			// ^^^^^^^^^^^^^^^^");
			if (u.isEnabled()) {
				// logger.debug("^^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^");
				jdbcUserDetailsManager.getJdbcTemplate()
						.update("update users set credentialsNonExpired = 1 where username = ?", u.getUsername());
			}
		}
	}

	//allows user to publish or make private 
	@Override
	public boolean hasGlobalAuthorities(String username) throws NoUserFoundException {
		User user = this.retrieveUserByUsername(username);
		if (user == null) {
			throw new NoUserFoundException("User could not be found");
		}
		if (isAdmin(username)) {
			return true;
		}

		Collection<GrantedAuthority> authorit = user.getAuthorities();
		for (GrantedAuthority auth : authorit) {
			if (PUBLIC_AUTHORITIES.contains(auth.getAuthority())) {
				return true;
			}
		}

		return false;
	}
	
	//list authorities
	@Override
	public List<String> getUserAuthorities(String username) throws NoUserFoundException {
		User user = this.retrieveUserByUsername(username);
		if (user == null) {
			throw new NoUserFoundException("User could not be found");
		}
		List<String> res = new ArrayList<String>();
		Collection<GrantedAuthority> authorit = user.getAuthorities();
		for (GrantedAuthority auth : authorit) {
			res.add(auth.getAuthority());
		}
		return res;
	}
	
	
	

	@Override
	public boolean isAdminByEmail(String email) throws NoUserFoundException {
		return this.adminEmails.contains(email);
	}

	@Override
	public boolean isAdmin(String username) throws NoUserFoundException {
		if (username == null) {
			return false;
		}		
		User user = this.retrieveUserByUsername(username);
		if (user == null) {			
			return false;
//			throw new NoUserFoundException("User could not be found");
		}
		
		Account account = accountService.findByTheAccountsUsername(username);
		if (account != null ) {
			if (account.getEmail() != null && this.adminEmails.contains(account.getEmail())) {
				return true;
			}
		}
		
		Collection<GrantedAuthority> authorit = user.getAuthorities();
		for (GrantedAuthority auth : authorit) {
			if (ADMIN_AUTHORITIES.contains(auth.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSupervisor(String username) throws NoUserFoundException {
		User user = this.retrieveUserByUsername(username);
		if (user == null) {
			throw new NoUserFoundException("User could not be found");
		}
		Collection<GrantedAuthority> authorit = user.getAuthorities();
		for (GrantedAuthority auth : authorit) {
			if (SUPERVISOR_AUTHORITY.equals(auth.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPublisher(String username) throws NoUserFoundException {
		User user = this.retrieveUserByUsername(username);
		if (user == null) {
			throw new NoUserFoundException("User could not be found");
		}
		Collection<GrantedAuthority> authorit = user.getAuthorities();
		for (GrantedAuthority auth : authorit) {
			if (PUBLISHER_AUTHORITY.equals(auth.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDeployer(String username) throws NoUserFoundException {
		User user = this.retrieveUserByUsername(username);
		if (user == null) {
			throw new NoUserFoundException("User could not be found");
		}
		Collection<GrantedAuthority> authorit = user.getAuthorities();
		for (GrantedAuthority auth : authorit) {
			if (DEPLOYER_AUTHORITY.equals(auth.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	
}
