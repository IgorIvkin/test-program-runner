package com.igorivkin.testprogramrunner.exception;

public class RunningTooLongException extends RuntimeException {

    public RunningTooLongException() {
        super();
    }

    public RunningTooLongException(String message) {
        super(message);
    }
}
