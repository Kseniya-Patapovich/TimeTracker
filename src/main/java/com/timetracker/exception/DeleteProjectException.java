package com.timetracker.exception;

public class DeleteProjectException extends RuntimeException {
    private String message;

    public DeleteProjectException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Only project with DRAFT status can be deleted! Project with id " + message + " is not DRAFT!";
    }
}
