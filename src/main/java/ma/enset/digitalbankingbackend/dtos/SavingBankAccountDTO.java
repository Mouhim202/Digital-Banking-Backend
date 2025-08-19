package ma.enset.digitalbankingbackend.dtos;


import lombok.Data;
import ma.enset.digitalbankingbackend.enums.AccountStatus;
import java.util.Date;



@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private CustomerDTO customerDTO;
    private AccountStatus status;
    private double interestRate;







    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setCustomer(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public CustomerDTO getCustomer() {
        return customerDTO;
    }

}
