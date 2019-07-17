package com.exception;

public class InvalidTransactionParametersException extends Exception {

    public InvalidTransactionParametersException() {
        super("Transaction was failed during parameters verification. Please, check all parameters.");
    }
}
