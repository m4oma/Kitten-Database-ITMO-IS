package ru.m4oma.Exceptions;

/**
 * Is thrown when we want to add account with name, which is already used
 */
public class AccountAlreadyExistsException extends Exception {
  public AccountAlreadyExistsException(String accountName) {
    super("Account " + accountName + "already exists");
  }
}
