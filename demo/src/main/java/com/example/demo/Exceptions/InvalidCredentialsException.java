package com.example.demo.Exceptions;

public class InvalidCredentialsException extends  RuntimeException{
    public InvalidCredentialsException(){
        super("Email or Password Invalid");
    }
}
