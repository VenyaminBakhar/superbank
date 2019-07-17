package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Account;
import com.model.CustomResponse;
import com.service.AccountCacheService;
import com.wrapper.AccountServiceHandleExceptionWrapper;


import static spark.Spark.*;


public class AccountController {
    private static final AccountCacheService accountCacheService = AccountCacheService.getAccountCacheService();
    private static final AccountServiceHandleExceptionWrapper accountServiceWrapper =
            AccountServiceHandleExceptionWrapper.getAccountServiceHandleExceptionWrapperInstance();

    public AccountController() {
        get("/accounts/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            CustomResponse cachedResponse = accountCacheService.getFromCache(id);

            if (cachedResponse != null) {
                res.status(cachedResponse.getStatus());
                return cachedResponse.getResponseMessage();
            }

            CustomResponse customResponse = accountServiceWrapper.getAccountByIdWithHandledExceptions(id);
            accountCacheService.addOrUpdateCache(id, customResponse);

            res.status(customResponse.getStatus());
            return customResponse.getResponseMessage();
        });

        post("/accounts", (req, res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.readValue(req.body(), Account.class);
            CustomResponse customResponse = accountServiceWrapper.createAccountWithHandledExceptions(account);

            if (customResponse.getStatus() == 201) {
                accountCacheService.deleteFromCache(account.getId());
            }

            res.status(customResponse.getStatus());
            return customResponse.getResponseMessage();
        });

        delete("/accounts/delete/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            CustomResponse customResponse = accountServiceWrapper.deleteAccountByIdWithHandledExceptions(id);

            if (customResponse.getStatus() == 204) {
                accountCacheService.deleteFromCache(id);
            }

            res.status(customResponse.getStatus());
            return customResponse.getResponseMessage();
        });
    }
}
