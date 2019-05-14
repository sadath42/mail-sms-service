package com.arzesh.exception;

public class SmsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SmsException(String message, Throwable e) {
	super(message, e);
    }

    public SmsException(String string) {
	super(string);
    }

}
