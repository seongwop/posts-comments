package com.example.postscomments.exception;

public class CustomException extends RuntimeException {

    public CustomException () {
        super();
    }

    public CustomException (String msg) {
        super(msg);
    }

    public CustomException (Throwable cause) {
        super(cause);
    }

    public CustomException (String msg, Throwable cause) {
        super(msg, cause);
    }
}
