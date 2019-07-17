package com.util;

import com.exception.CreateAccountException;
import com.model.Account;
import com.service.AccountService;

public class AccountTestUtil {
    private static final AccountService accountService = AccountService.getAccountServiceInstance();

    public static void populateAccountDBForAccountAPITests() throws CreateAccountException {
        Account first = new Account();
        first.setHolderName("ACCOUNT 1 HOLDER");
        first.setBalance(1234.459);
        accountService.createAccount(first);

        Account second = new Account();
        second.setHolderName("ACCOUNT 2 HOLDER");
        second.setBalance(12378.2);
        accountService.createAccount(second);

        Account third = new Account();
        third.setHolderName("ACCOUNT 3 HOLDER");
        third.setBalance(12378);
        accountService.createAccount(third);
    }

    public static void populateAccountDBForTransactionAPITests() throws CreateAccountException {
        Account first = new Account();
        first.setHolderName("ACCOUNT 1 TRANSACTION TEST");
        first.setBalance(10000000.1);
        accountService.createAccount(first);

        Account second = new Account();
        second.setHolderName("ACCOUNT 2 HOLDER TRANSACTION TEST");
        second.setBalance(12378000.2);
        accountService.createAccount(second);

        Account third = new Account();
        third.setHolderName("ACCOUNT 3 HOLDER TRANSACTION TEST");
        third.setBalance(1000);
        accountService.createAccount(third);

        Account fours = new Account();
        fours.setHolderName("ACCOUNT 4 HOLDER TRANSACTION TEST");
        fours.setBalance(100);
        accountService.createAccount(fours);
    }

    public static Account getAccountFromDBById(int id) {
        return accountService.getAccountById(id);
    }

    public static Account createAccount(int id, String holderName, double balance) {
        Account account = new Account();
        account.setId(id);
        account.setHolderName(holderName);
        account.setBalance(balance);

        return account;
    }
}
