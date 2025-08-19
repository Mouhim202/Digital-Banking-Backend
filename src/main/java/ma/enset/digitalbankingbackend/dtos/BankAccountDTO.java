package ma.enset.digitalbankingbackend.dtos;

import lombok.Data;

@Data
public class BankAccountDTO {
    private String type;

    public void setType(String type) {
        this.type = type;
    }
}
