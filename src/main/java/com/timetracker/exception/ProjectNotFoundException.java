package com.timetracker.exception;

public class ProjectNotFoundException extends RuntimeException {
    private String message;

    public ProjectNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Project with id " + message + " not found!";
    }
}
