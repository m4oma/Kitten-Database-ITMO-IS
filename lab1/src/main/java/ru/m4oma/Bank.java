package ru.m4oma;

import ru.m4oma.Exceptions.AccountAlreadyExistsException;
import ru.m4oma.Exceptions.AccountNotFoundException;

import java.util.HashMap;

/**
 * Stores accounts and provides access to them
 */
public class Bank {
    private final HashMap<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public Account getAccount(String name) throws AccountNotFoundException {
        if (accounts.containsKey(name)) {
            return accounts.get(name);
        }
        throw new AccountNotFoundException(name);
    }

    public void addAccount(String name) throws AccountAlreadyExistsException {
        if (!accounts.containsKey(name)) {
            accounts.put(name, new Account(name));
        } else {
            throw new AccountAlreadyExistsException(name);
        }
    }
}
