package com.arzesh.model;

import javax.validation.constraints.NotEmpty;

public class MailRequest {

    @NotEmpty
    private String toAddress;

    public String getToAddress() {
	return toAddress;
    }

    public void setToAddress(String toAddress) {
	this.toAddress = toAddress;
    }

}
