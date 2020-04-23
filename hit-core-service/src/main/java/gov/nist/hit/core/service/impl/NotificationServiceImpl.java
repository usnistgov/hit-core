package gov.nist.hit.core.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.nist.hit.core.domain.Notification;
import gov.nist.hit.core.repo.NotificationRepository;
import gov.nist.hit.core.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;



  @Override
  public Notification findOne(Long id) {
    return notificationRepository.findOne(id);
  }

  @Override
  @Transactional(value = "transactionManager")
  public void save(Notification notification) {
	  notification.setDate(new Date());
	  notificationRepository.saveAndFlush(notification);
  }


@Override
public Notification findLastest() {
	return notificationRepository.findLastest();
}

@Override
public List<Notification> findActives() {
	return notificationRepository.findActives();
}

@Override
public List<Notification> findAll() {
	return notificationRepository.findAll();
}

}
