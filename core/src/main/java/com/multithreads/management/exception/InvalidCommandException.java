package com.multithreads.management.exception;

/**
 * Exception that is thrown in case of commands invalidity.
 */
public class InvalidCommandException extends Exception {

    /**
     * Initializes message variable in the superclass.
     *
     * @param message exception message
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
