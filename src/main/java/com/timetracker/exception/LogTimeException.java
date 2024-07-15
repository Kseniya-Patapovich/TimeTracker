package com.timetracker.exception;

public class LogTimeException extends RuntimeException {
    private String message;

    public LogTimeException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Cannot log more than 12 hours in a day! Today spent " + message + " minutes!";
    }
}
