package com.wrapper;

import com.exception.CreateAccountException;
import com.exception.DeleteAccountException;
import com.model.Account;
import com.model.CustomResponse;
import com.service.AccountService;

import static com.constants.MessageConstants.ACC_NOT_FOUND;

public class AccountServiceHandleExceptionWrapper {
    private final AccountService accountService = AccountService.getAccountServiceInstance();

    private static volatile AccountServiceHandleExceptionWrapper accountServiceHandleExceptionWrapperInstance;

    private AccountServiceHandleExceptionWrapper() {}

    public static AccountServiceHandleExceptionWrapper getAccountServiceHandleExceptionWrapperInstance() {
        AccountServiceHandleExceptionWrapper localInstance = accountServiceHandleExceptionWrapperInstance;
        if (localInstance == null) {
            synchronized (AccountServiceHandleExceptionWrapper.class) {
                localInstance = accountServiceHandleExceptionWrapperInstance;
                if (localInstance == null) {
                    accountServiceHandleExceptionWrapperInstance = localInstance = new AccountServiceHandleExceptionWrapper();
                }
            }
        }
        return localInstance;
    }


    public CustomResponse createAccountWithHandledExceptions(Account account) {
        try {
            accountService.createAccount(account);
        } catch (CreateAccountException e) {
            return new CustomResponse(500, e.getMessage());
        } catch (IllegalArgumentException e) {
            return new CustomResponse(400, e.getMessage());
        }

        return new CustomResponse(201, account.toString());
    }

    public CustomResponse getAccountByIdWithHandledExceptions(int id) {
        Account account = accountService.getAccountById(id);
        return account != null ? new CustomResponse(200, account.toString()) : new CustomResponse(404, ACC_NOT_FOUND);
    }

    public CustomResponse deleteAccountByIdWithHandledExceptions(int id) {
        Account account = accountService.getAccountById(id);

        if (account == null) {
            return new CustomResponse(404, ACC_NOT_FOUND);
        }

        try {
            accountService.deleteAccount(account);
        } catch (DeleteAccountException e) {
            return new CustomResponse(500, e.getMessage());
        }

        return new CustomResponse(204, "");
    }

}
