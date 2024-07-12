package com.timetracker.exception;
public class SameUserInDatabase extends RuntimeException {
    String message;

    public SameUserInDatabase(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Registration problem! We already have user with login: " + message;
    }
}
