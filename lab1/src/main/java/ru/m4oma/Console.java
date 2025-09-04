package ru.m4oma;

import ru.m4oma.Exceptions.AccountAlreadyExistsException;
import ru.m4oma.Exceptions.AccountNotFoundException;
import ru.m4oma.Exceptions.WithdrawException;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Our presentation
 */
public class Console {
    private final Scanner scanner;
    private Account currentAccount;
    private final Bank bank;

    public Console() {
        this.scanner = new Scanner(System.in);
        this.currentAccount = null;
        this.bank = new Bank();
    }

    /**
     * What a user can do if he isn't logged
     */
    public void DefaultScenario() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Создать аккаунт");
        System.out.println("2. Войти в аккаунт");
        System.out.println("3. Выход из системы");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Введите ваше имя");
                try {
                    bank.addAccount(scanner.next());
                } catch (AccountAlreadyExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> {
                System.out.println("Введите имя аккаунта");
                String accountName = scanner.next();
                try {
                    currentAccount = bank.getAccount(accountName);
                } catch (AccountNotFoundException e) {
                    System.out.println("No such account");
                }
            }
            case 3 -> {
                System.out.println("До свидания");
                System.exit(0);
            }
            default -> System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }

    /**
     * What a user can do if he is logged in
     */
    public void LoggedUserScenario() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Просмотр баланса");
        System.out.println("2. Пополнение счета");
        System.out.println("3. Снятие денег");
        System.out.println("4. История операций");
        System.out.println("5. Выход из аккаунта");
        System.out.println("6. Выход из системы");
        System.out.print("Введите номер операции: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> System.out.println("Текущий баланс: " + currentAccount.getBalance());
            case 2 -> {
                System.out.print("Введите сумму пополнения: ");
                BigDecimal amount = scanner.nextBigDecimal();
                try {
                    bank.getAccount(currentAccount.getName()).deposit(amount);
                } catch (AccountNotFoundException e) {
                    System.out.println("No such account");
                }
                System.out.println("Счет пополнен на " + amount);
            }
            case 3 -> {
                System.out.print("Введите сумму для снятия: ");
                BigDecimal amount = scanner.nextBigDecimal();
                try {
                    try {
                        bank.getAccount(currentAccount.getName()).withdraw(amount);
                    } catch (AccountNotFoundException e) {
                        System.out.println("No such account");
                    }
                    System.out.println("Вы сняли " + amount);
                } catch (WithdrawException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case 4 -> {
                System.out.println("История операций:");
                try {
                    bank.getAccount(currentAccount.getName()).showAccountHistory();
                } catch (AccountNotFoundException e) {
                    System.out.println("No such account");
                }
            }
            case 5 -> {
                System.out.println("Выход из аккаунта.");
                currentAccount = null;
            }
            case 6 -> {
                System.out.println("До свидания");
                System.exit(0);
            }
            default -> System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }


    public void start() {
        while (true) {
            if (currentAccount == null) {
                DefaultScenario();
            } else {
                LoggedUserScenario();
            }
        }
    }
}