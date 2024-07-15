package com.timetracker.exception;

public class ProjectIsNotInProgressException extends RuntimeException {
    private String message;

    public ProjectIsNotInProgressException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Project with id " + message + " is not in progress!";
    }
}
