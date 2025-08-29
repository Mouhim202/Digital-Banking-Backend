package ma.enset.digitalbankingbackend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import ma.enset.digitalbankingbackend.entities.BankAccount;
import ma.enset.digitalbankingbackend.enums.OperationType;

import java.util.Date;


@Data

public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private  double amount;
    private OperationType type;
    private String description;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setType(OperationType type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public double getAmount() {
        return amount;
    }

    public OperationType getType() {
        return type;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }



}
