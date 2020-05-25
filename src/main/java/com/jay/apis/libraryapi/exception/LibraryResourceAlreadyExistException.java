package com.jay.apis.libraryapi.exception;

public class LibraryResourceAlreadyExistException extends RuntimeException   {
    private String traceId;

    public LibraryResourceAlreadyExistException(String traceId, String message) {
        super(message);
        this.traceId = traceId;
    }

    public LibraryResourceAlreadyExistException(String message) {
        super(message);
    }

    public String getTraceId() {
        return traceId;
    }
}
