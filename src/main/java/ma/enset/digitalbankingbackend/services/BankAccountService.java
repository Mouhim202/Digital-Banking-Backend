package ma.enset.digitalbankingbackend.services;

import jakarta.transaction.Transactional;
import ma.enset.digitalbankingbackend.dtos.CustomerDTO;
import ma.enset.digitalbankingbackend.entities.BankAccount;
import ma.enset.digitalbankingbackend.entities.CurrentAccount;
import ma.enset.digitalbankingbackend.entities.Customer;
import ma.enset.digitalbankingbackend.entities.SavingAccount;
import ma.enset.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbankingbackend.exceptions.CustomerNotFoundExcel;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundExcel;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundExcel;
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();
}
