package gov.nist.healthcare.tools.core.repo;

   
import gov.nist.healthcare.tools.core.models.TransactionStatus;
import gov.nist.healthcare.tools.core.models.ConnectivityTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConnectivityTransactionRepository extends
		JpaRepository<ConnectivityTransaction, Long> {

	@Query("select incoming from ConnectivityTransaction transaction where transaction.user.id = :userId")
	String getIncomingMessageByUserId(@Param("userId") Long userId);
	
	@Query("select outgoing from ConnectivityTransaction transaction where transaction.user.id = :userId")
	String getOutgoingMessageByTokenId(@Param("userId") Long userId);
	
 	@Query("select transaction from ConnectivityTransaction transaction where transaction.user.username = :username and transaction.user.password = :password")
 	ConnectivityTransaction findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	@Query("select transaction from ConnectivityTransaction transaction where transaction.user.id = :userId")
	ConnectivityTransaction findOneByUserId(@Param("userId") Long userId);
	
//	@Query("select transaction.user.tokenId from ConnectivityTransaction transaction where transaction.user.username = :username")
//	String findTokenIdByUsername(@Param("username") String username);
//	
//	
	@Query("select transaction.status from ConnectivityTransaction transaction where transaction.user.username = :username and transaction.user.password = :password")
	TransactionStatus getStatusByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
	
	
	
}
