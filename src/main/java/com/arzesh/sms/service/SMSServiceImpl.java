package com.arzesh.sms.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.arzesh.config.AWSProperties;
import com.arzesh.exception.SmsException;

@Service
public class SMSServiceImpl implements SMSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSServiceImpl.class);

    @Autowired
    private AWSProperties awsProperties;
    private Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();

    @PostConstruct
    private void init() {
	smsAttributes.put("AWS.SNS.SMS.SenderID",
		new MessageAttributeValue().withStringValue(awsProperties.getSenderID()).withDataType("String"));
	smsAttributes.put("AWS.SNS.SMS.MaxPrice",
		new MessageAttributeValue().withStringValue(awsProperties.getMaxPrice()).withDataType("Number"));
	smsAttributes.put("AWS.SNS.SMS.SMSType",
		new MessageAttributeValue().withStringValue(awsProperties.getSMSType()).withDataType("String"));
    }

    @Override
    public String sendSMS() {
	String messageId = null;
	try {
	    // Since ec2 credentials are rotated every 2 hrs.
	    AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
		    .withCredentials(new InstanceProfileCredentialsProvider(false)).build();

	    String phoneNumber = awsProperties.getPhoneNumber();
	    PublishResult result = snsClient.publish(new PublishRequest().withMessage(awsProperties.getText())
		    .withPhoneNumber(phoneNumber).withMessageAttributes(smsAttributes));
	    messageId = result.getMessageId();
	    LOGGER.info("Message sent to the subscriber {} with details {}", result, smsAttributes);

	} catch (SdkClientException e) {
	    LOGGER.error("Error while sending the message {}", e);
	    throw new SmsException("Error while sending the message", e);
	}
	return messageId;
    }

}
