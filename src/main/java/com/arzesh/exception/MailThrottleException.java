package com.arzesh.exception;

public class MailThrottleException extends MsgSenderException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MailThrottleException(String msg) {
	super(msg);
    }
    

}
