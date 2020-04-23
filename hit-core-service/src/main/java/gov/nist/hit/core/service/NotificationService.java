package gov.nist.hit.core.service;

import java.util.List;

import gov.nist.hit.core.domain.Notification;

public interface NotificationService {

  public Notification findOne(Long id);

  void save(Notification notification);

  public Notification findLastest();
  
  public List<Notification> findActives();
  
  public List<Notification> findAll();

}
