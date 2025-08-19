package ma.enset.digitalbankingbackend.web;

import ma.enset.digitalbankingbackend.dtos.BankAccountDTO;
import ma.enset.digitalbankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountRestAPI {

    private BankAccountService bankAccountService;
    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccountById(String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);

    }
    @GetMapping("accounts")
    public List<BankAccountDTO> getBankAccounts()  {
        return  bankAccountService.bankAccountList();
    }

}
