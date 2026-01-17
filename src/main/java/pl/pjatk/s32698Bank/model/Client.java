package pl.pjatk.s32698Bank.model;

import java.math.BigDecimal;

public class Client {
    private Long id;
    private BigDecimal balance;

    public Client(Long id, BigDecimal balance) {
        this.id = id;
        setBalance(balance);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Cannot set a null balance!!!");
        }
        if (balance.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Balance cannot have more than 2 decimal places!");
        }
        this.balance = balance;
    }
}