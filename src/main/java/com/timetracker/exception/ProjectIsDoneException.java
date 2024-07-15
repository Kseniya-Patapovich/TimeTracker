package com.timetracker.exception;

public class ProjectIsDoneException extends RuntimeException {
    private String message;

    public ProjectIsDoneException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Project with id " + message + " is done!";
    }
}
