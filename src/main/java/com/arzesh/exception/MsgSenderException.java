package com.arzesh.exception;

public class MsgSenderException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MsgSenderException(String message, Throwable e) {
	super(message, e);
    }

    public MsgSenderException(String string) {
	super(string);
    }

}
