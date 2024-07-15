package com.timetracker.exception;

public class UserNotFoundException extends RuntimeException {
    private String message;

    public UserNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "User with id " + message + " not found!";
    }
}
