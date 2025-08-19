package ma.enset.digitalbankingbackend.services;

import jakarta.transaction.Transactional;
import ma.enset.digitalbankingbackend.dtos.BankAccountDTO;
import ma.enset.digitalbankingbackend.dtos.CurrentBankAccountDTO;
import ma.enset.digitalbankingbackend.dtos.CustomerDTO;
import ma.enset.digitalbankingbackend.dtos.SavingBankAccountDTO;
import ma.enset.digitalbankingbackend.entities.*;
import ma.enset.digitalbankingbackend.exceptions.CustomerNotFoundException;
import ma.enset.digitalbankingbackend.enums.OperationType;
import ma.enset.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbankingbackend.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbankingbackend.exceptions.CustomerNotFoundExcel;
import ma.enset.digitalbankingbackend.mappers.BankAccountMapperImpl;
import ma.enset.digitalbankingbackend.repositories.AccountOperationRepository;
import ma.enset.digitalbankingbackend.repositories.BankAccountRepository;
import ma.enset.digitalbankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional


public class BankAccountServiceImpl implements BankAccountService {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;

    private final BankAccountMapperImpl dtoMapper;
    private static final Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository, BankAccountMapperImpl dtoMapper) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundExcel, CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(null);
        if(customer==null)
         throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount=bankAccountRepository.save(currentAccount);

        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundExcel {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundExcel("Customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        return dtoMapper.fromSavingBankAccount(savingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
//        List<CustomerDTO> customerDTOs = new ArrayList<>();
//        for(Customer customer : customers) {
//            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
//            customerDTOs.add(customerDTO);
//        }
        return customerDTOs;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount ){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else {
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts= bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS=bankAccounts.stream().map(bankAccount -> {

            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount=(SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            }else {
                CurrentAccount currentAccount=(CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId)
                .orElseThrow(() ->new CustomerNotFoundException("Customer Not Found"));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void  deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);

    }
}
