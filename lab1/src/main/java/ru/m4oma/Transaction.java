package ru.m4oma;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


/**
 * Transaction type that we send to accounts
 */
@Data
@AllArgsConstructor
public class Transaction {
    private final TransactionType type;
    private final BigDecimal amount;

    @Override
    public String toString() {
        return type + ": " + amount;
    }
}
