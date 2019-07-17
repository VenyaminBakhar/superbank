package com.wrapper;

import com.exception.InvalidTransactionParametersException;
import com.model.CustomResponse;
import com.model.TransactionModel;
import com.service.TransactionService;

import java.util.List;

import static com.constants.MessageConstants.*;

public class TransactionServiceHandleExceptionWrapper {
    private static final TransactionService transactionService = TransactionService.getTransactionServiceInstance();

    private static volatile TransactionServiceHandleExceptionWrapper transactionServiceHandleExceptionWrapperInstance;

    private TransactionServiceHandleExceptionWrapper() {}

    public static TransactionServiceHandleExceptionWrapper getTransactionServiceHandleExceptionInstance() {
        TransactionServiceHandleExceptionWrapper localInstance = transactionServiceHandleExceptionWrapperInstance;
        if (localInstance == null) {
            synchronized (TransactionServiceHandleExceptionWrapper.class) {
                localInstance = transactionServiceHandleExceptionWrapperInstance;
                if (localInstance == null) {
                    transactionServiceHandleExceptionWrapperInstance = localInstance = new TransactionServiceHandleExceptionWrapper();
                }
            }
        }
        return localInstance;
    }


    public CustomResponse doTransactionWithHandledExceptions(TransactionModel transactionModel) {
        try {
            transactionService.doTransaction(transactionModel);
        } catch (InvalidTransactionParametersException e) {
            return new CustomResponse(400, e.getMessage());
        } catch (Exception e) {
            return new CustomResponse(500, TRANSACTION_FAILED + e.getMessage());
        }

        return new CustomResponse(200, TRANSACTION_SUCCESS);
    }

    public CustomResponse getTransactionHistoryWithHandledExceptions(int accountId) {
        List<TransactionModel> results;

        try {
            results = transactionService.getTransactionHistory(accountId);
        } catch (IllegalArgumentException e) {
            return new CustomResponse(404, TRANSACTION_HISTORY_NOT_FOUND);
        } catch (Exception e) {
            return new CustomResponse(500, e.getMessage());
        }

        return new CustomResponse(200, results.toString());
    }
}
