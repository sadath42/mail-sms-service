package com.arzesh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("aws.sns.msg")
public class AWSProperties {

    private String SenderID;
    private String MaxPrice;
    private String SMSType;
    private String PhoneNumber;
    private String text;
    private String emailSubject;
    private String emailBody;
    private String fromAddress;

    public String getFromAddress() {
	return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
	this.fromAddress = fromAddress;
    }

    public String getEmailSubject() {
	return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
	this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
	return emailBody;
    }

    public void setEmailBody(String emailBody) {
	this.emailBody = emailBody;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getSenderID() {
	return SenderID;
    }

    public void setSenderID(String senderID) {
	SenderID = senderID;
    }

    public String getMaxPrice() {
	return MaxPrice;
    }

    public void setMaxPrice(String maxPrice) {
	MaxPrice = maxPrice;
    }

    public String getSMSType() {
	return SMSType;
    }

    public void setSMSType(String sMSType) {
	SMSType = sMSType;
    }

    public String getPhoneNumber() {
	return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	PhoneNumber = phoneNumber;
    }

}
