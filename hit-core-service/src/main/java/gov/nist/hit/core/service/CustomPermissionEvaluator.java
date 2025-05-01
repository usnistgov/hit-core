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
package gov.nist.hit.core.service;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import gov.nist.auth.hit.core.domain.Account;

/**
 * @author fdevaulx
 * 
 */
@Component(value = "customPermissionEvaluator")
public class CustomPermissionEvaluator implements PermissionEvaluator {

	static final Logger logger = LogManager.getLogger(CustomPermissionEvaluator.class);

  @Autowired
  AccountService accountService;

  @Autowired
  AppInfoService appInfoService;

  //check if authentication matches the id or is admin

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.access.PermissionEvaluator#hasPermission
   * (org.springframework.security.core.Authentication, java.lang.Object, java.lang.Object)
   */
  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    logger.debug("^^^^^^^^^^^^^^^^^ 0 ^^^^^^^^^^^^^^^^^^");
    if ("accessAccountBasedResource".equals(permission)) {
      logger.debug("^^^^^^^^^^^^^^^^^ 1 ^^^^^^^^^^^^^^^^^^");
      Account acc = accountService.findByTheAccountsUsername(authentication.getName());
      logger.debug("^^^^^^^^^^^^^^^^^ 2 " + acc + " ^^^^^^^^^^^^^^^^^^");
      if (acc == null) {
        return false;
      }
      logger.debug("^^^^^^^^^^^^^^^^^ 3 acc.getId(): " + acc.getId() + " targetDomainObject: "
          + targetDomainObject + " ^^^^^^^^^^^^^^^^^^");
      if (acc.getId().equals(targetDomainObject)
          || appInfoService.get().getAdminEmails().contains(acc.getEmail())
          || authentication.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
        return true;
      }
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.access.PermissionEvaluator#hasPermission
   * (org.springframework.security.core.Authentication, java.io.Serializable, java.lang.String,
   * java.lang.Object)
   */
  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {

    if ("accessAccountBasedResource".equals(permission)) {
      Account acc = accountService.findByTheAccountsUsername(authentication.getName());
      if (acc == null) {
        return false;
      }
      if (acc.getId() == targetId || appInfoService.get().getAdminEmails().contains(acc.getEmail())
          || authentication.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
        return true;
      }
    }

    return false;
  }

}
