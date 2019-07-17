package com.exception;

public class CreateAccountException extends Exception {
    public CreateAccountException(Exception e) { super( "Account creation failed." + e.getMessage());}
}
