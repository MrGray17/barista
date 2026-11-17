package com.barista.job;

public class RetryableJobException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RetryableJobException(String message) {
        super(message);
    }

    public RetryableJobException(String message, Throwable cause) {
        super(message, cause);
    }
}