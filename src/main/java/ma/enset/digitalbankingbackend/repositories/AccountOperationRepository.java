package ma.enset.digitalbankingbackend.repositories;

import ma.enset.digitalbankingbackend.entities.AccountOperation;
import ma.enset.digitalbankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

}
