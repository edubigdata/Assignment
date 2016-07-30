package com.assignment.exception;

/**
 * Created by deokishore on 18/01/2016.
 */
public class EduBigDataException extends Exception {

    public EduBigDataException() {
    }

    public EduBigDataException(String message) {
        super(message);
    }

    public EduBigDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public EduBigDataException(Throwable cause) {
        super(cause);
    }

    public EduBigDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
