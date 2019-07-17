package com.exception;

public class DeleteAccountException extends Exception {
    public DeleteAccountException(Exception e) {
        super("Account deletion failed. " + e.getMessage());
    }
}
