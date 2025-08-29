package ma.enset.digitalbankingbackend.repositories;

import ma.enset.digitalbankingbackend.entities.AccountOperation;
import ma.enset.digitalbankingbackend.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    public List<AccountOperation> findByBankAccount(BankAccount bankAccount);

    List<AccountOperation> findByBankAccountId(String accountId);
    Page<AccountOperation> findByBankAccountId(String accountId, Pageable pageable);
}
