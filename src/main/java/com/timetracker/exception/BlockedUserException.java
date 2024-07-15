package com.timetracker.exception;

public class BlockedUserException extends RuntimeException {
    private String message;

    public BlockedUserException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "User with id= " + message + " is blocked!";
    }
}
