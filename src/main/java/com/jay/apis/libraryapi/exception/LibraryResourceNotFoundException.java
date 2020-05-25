package com.jay.apis.libraryapi.exception;

public class LibraryResourceNotFoundException extends Throwable {

    private String traceId;

    public LibraryResourceNotFoundException(String traceId, String message) {
        super(message);
        this.traceId = traceId;
    }

    public LibraryResourceNotFoundException(String message) {
        super(message);
    }

    public String getTraceId() {
        return traceId;
    }
}
