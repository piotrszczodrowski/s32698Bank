package pl.pjatk.s32698Bank.model;

import java.math.BigDecimal;

public class TransactionResult {
    private final TransactionStatus status;
    private final BigDecimal newBalance;

    public TransactionResult(TransactionStatus status, BigDecimal newBalance) {
        this.status = status;
        this.newBalance = newBalance;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }
}