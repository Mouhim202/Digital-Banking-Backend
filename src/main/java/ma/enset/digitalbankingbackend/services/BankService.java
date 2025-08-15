package ma.enset.digitalbankingbackend.services;

import jakarta.transaction.Transactional;
import ma.enset.digitalbankingbackend.entities.BankAccount;
import ma.enset.digitalbankingbackend.entities.CurrentAccount;
import ma.enset.digitalbankingbackend.entities.SavingAccount;
import ma.enset.digitalbankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount=bankAccountRepository.findById("27b64999-e887-4430-a8aa-8137fe353c5e").orElse(null);
        if(bankAccount!=null) {
            System.out.println("*******************************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft=>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rat=>" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());

            });
        }

    }
}
