package com.greenhat.orm.exception;

/**
 * 主键找不到报错
 */
public class PkNotFoundException extends RuntimeException {
    public PkNotFoundException() {
        super();
    }

    public PkNotFoundException(String message) {
        super(message);
    }

    public PkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PkNotFoundException(Throwable cause) {
        super(cause);
    }
}
