import org.junit.jupiter.api.Test;
import ru.m4oma.Account;
import ru.m4oma.Bank;
import ru.m4oma.Exceptions.AccountAlreadyExistsException;
import ru.m4oma.Exceptions.AccountNotFoundException;
import ru.m4oma.Exceptions.WithdrawException;
import ru.m4oma.Transaction;
import ru.m4oma.TransactionType;

import java.math.BigDecimal;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class BankTests {
    private final Bank bank = new Bank();

    @Test
    void accountPushToBankSuccessfulTest() throws AccountAlreadyExistsException, AccountNotFoundException {
        bank.addAccount("aboba");
        assertEquals(bank.getAccount("aboba"), new Account("aboba"));
    }

    @Test
    void alreadyExistingAccountPushTest() throws AccountAlreadyExistsException {
        bank.addAccount("aboba");
        assertThrows(AccountAlreadyExistsException.class, () -> bank.addAccount("aboba"));
    }

    @Test
    void notExistingAccountCallTest() throws AccountAlreadyExistsException {
        bank.addAccount("aboba");
        assertThrows(AccountNotFoundException.class, () -> bank.getAccount("aaboba"));
    }
    @Test
    void depositTest() {
        Account account = new Account("aboba");
        account.deposit(BigDecimal.valueOf(10));
        assertEquals(account.getBalance(), BigDecimal.valueOf(10));
    }

    @Test
    void withdrawSuccessfulTest() {
        Account account = new Account("aboba");
        account.deposit(BigDecimal.valueOf(10));
        account.withdraw(BigDecimal.valueOf(5));
        assertEquals(account.getBalance(), BigDecimal.valueOf(5));
    }

    @Test
    void withdrawFailedTest() {
        Account account = new Account("aboba");
        account.deposit(BigDecimal.valueOf(10));
        assertThrows(WithdrawException.class, () -> account.withdraw(BigDecimal.valueOf(15)));
    }

    @Test
    void showHistoryTest() {
        Account account = new Account("aboba");
        account.deposit(BigDecimal.valueOf(10));
        account.withdraw(BigDecimal.valueOf(5));
        Vector<Transaction> transactionVector = new Vector<>();
        transactionVector.add(new Transaction(TransactionType.Deposit, BigDecimal.valueOf(10)));
        transactionVector.add(new Transaction(TransactionType.Withdraw, BigDecimal.valueOf(5)));
        assertEquals(account.getTransactions(), transactionVector);
    }
}
