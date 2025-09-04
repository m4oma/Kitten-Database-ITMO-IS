package ru.m4oma.Exceptions;

/**
 * Is thrown when we want to get access to absent account
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountName) {
        super("Account " + accountName + " not found");
    }
}
