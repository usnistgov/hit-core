package gov.nist.hit.core.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gov.nist.hit.core.domain.Transaction;

public interface TransactionRepository
    extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

  @Query("select transaction.incoming from Transaction transaction where transaction.userId = :userId and transaction.testStepId = :testStepId")
  String getIncomingMessageByUserIdAndTestStepId(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction.outgoing from Transaction transaction where transaction.userId = :userId and transaction.testStepId = :testStepId")
  String getOutgoingMessageByUserIdAndTestStepId(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction from Transaction transaction where transaction.userId = :userId and transaction.testStepId = :testStepId")
  Transaction findOneByUserAndTestStep(@Param("userId") Long userId,
      @Param("testStepId") Long testStepId);

  @Query("select transaction from Transaction transaction where transaction.userId = :userId")
  List<Transaction> findAllByUser(@Param("userId") Long userId);
  
  //doesn't work for now
  @Query("select transaction from Transaction transaction INNER JOIN transaction.properties p1 INNER JOIN transaction.properties p2 INNER JOIN transaction.properties p3  WHERE (KEY(p1) = 'username' AND  VALUE(p1) = :username) AND   (KEY(p2) = 'password' AND  VALUE(p2) = :password)  AND  (KEY(p3) = 'facilityID' AND  VALUE(p3) = :facilityID)")
//  @Query("select transaction from Transaction transaction INNER JOIN transaction.properties p1  WHERE (KEY(p1) = 'username' AND  KEY(p1) = :username) ")
//  Transaction findOneByProperties(@Param("username") String username);
  List<Transaction> findOneByProperties(@Param("username") String username,@Param("password") String d,@Param("facilityID") String facilityID);

}
