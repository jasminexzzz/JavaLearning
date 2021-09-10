package com.jasmine.es.client.exception;

/**
 * @author wangyf
 * @since 1.2.2
 */
public class EsException extends RuntimeException {

    public EsException() {
        super();
    }

    public EsException(String message) {
        super(message);
    }

    public EsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsException(Throwable cause) {
        super(cause);
    }

    protected EsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
