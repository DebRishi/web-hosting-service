package com.debmallick.hosting.constant;

public enum Status {

    PENDING("Pending"),
    RUNNING("Running"),
    FAILURE("Failure"),
    SUCCESS("Success");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
