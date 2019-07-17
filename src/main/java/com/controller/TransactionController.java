package com.controller;

import com.wrapper.TransactionServiceHandleExceptionWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.model.CustomResponse;
import com.model.TransactionModel;


import static spark.Spark.get;
import static spark.Spark.post;

public class TransactionController {

    private static final TransactionServiceHandleExceptionWrapper transactionServiceWrapper =
            TransactionServiceHandleExceptionWrapper.getTransactionServiceHandleExceptionInstance();

    public TransactionController() {
        post("/transactions", (req, res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            TransactionModel transactionModel = objectMapper.readValue(req.body(), TransactionModel.class);
            CustomResponse customResponse = transactionServiceWrapper.doTransactionWithHandledExceptions(transactionModel);
            res.status(customResponse.getStatus());
            return customResponse.getResponseMessage();
        });

        get("/transactions/history/:accountId", (req, res) -> {
            int accountId = Integer.parseInt(req.params(":accountId"));
            CustomResponse customResponse = transactionServiceWrapper.getTransactionHistoryWithHandledExceptions(accountId);
            res.status(customResponse.getStatus());
            return customResponse.getResponseMessage();
        });
    }
}
