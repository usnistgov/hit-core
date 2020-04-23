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

package gov.nist.hit.core.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Transactional(value = "transactionManager")
	@Query(nativeQuery = true, value = "SELECT * FROM Notification n ORDER BY n.date DESC LIMIT 1")
	public Notification findLastest();

	@Transactional(value = "transactionManager")
	@Query(value = "SELECT n FROM Notification n WHERE active IS TRUE ORDER BY n.date DESC")
	public List<Notification> findActives();
	
	@Transactional(value = "transactionManager")
	@Query(value = "SELECT n FROM Notification n ORDER BY n.date DESC")
	public List<Notification> findAll();

}
