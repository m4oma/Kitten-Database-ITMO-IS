package ru.m4oma;

import lombok.Data;
import lombok.Getter;
import ru.m4oma.Exceptions.WithdrawException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * User account class
 */
@Getter
@Data
public class Account {
    private final String name;
    private BigDecimal balance;
    private ArrayList<Transaction> transactions;

    public Account(String accountName) {
        this.name = accountName;
        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.valueOf(0);
    }

    /**
     * Method with deposit logic
     *
     * @param amount how much we add
     */
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
        transactions.add(new Transaction(TransactionType.Deposit, amount));
    }

    /**
     * Method with withdraw logic
     *
     * @param amount how much we withdraw
     * @throws WithdrawException is thrown when we may have negative balance
     */
    public void withdraw(BigDecimal amount) throws WithdrawException {
        if (amount.compareTo(balance) < 0) {
            balance = balance.subtract(amount);
            transactions.add(new Transaction(TransactionType.Withdraw, amount));
        } else {
            throw new WithdrawException();
        }
    }

    /**
     * Shows transaction history of this account
     */
    public void showAccountHistory() {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

}
