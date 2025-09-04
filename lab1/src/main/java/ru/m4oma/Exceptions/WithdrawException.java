package ru.m4oma.Exceptions;

/**
 * Is thrown when we want to withdraw more than we have on account
 */
public class WithdrawException extends RuntimeException {
    public WithdrawException() {
        super("You can't withdraw more than you have");
    }
}
