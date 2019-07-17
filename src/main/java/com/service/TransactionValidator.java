package com.service;

import com.model.Account;
import com.model.TransactionModel;

class TransactionValidator {
    private static final AccountService accountService = AccountService.getAccountServiceInstance();

    static boolean isValidTransaction(TransactionModel transactionModel) {
        if (!transactionModel.isWellFormed()) return false;

        Account accountFrom = accountService.getAccountById(transactionModel.getIdFrom());
        if (accountFrom == null) return false;

        if (accountFrom.getBalance() < transactionModel.getAmount()) return false;

        Account accountTo = accountService.getAccountById(transactionModel.getIdTo());
        return accountTo != null;
    }
}
