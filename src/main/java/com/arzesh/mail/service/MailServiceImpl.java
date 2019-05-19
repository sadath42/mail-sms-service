package com.arzesh.mail.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.arzesh.config.AWSProperties;
import com.arzesh.exception.MailThrottleException;
import com.arzesh.exception.MsgSenderException;
import com.google.common.util.concurrent.RateLimiter;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private AWSProperties awsProperties;
    private Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
    private RateLimiter limiter;
    String HTMLBODY;

    @PostConstruct
    private void init() {
	// The HTML body for the email.
	HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>" + "<p>" + awsProperties.getEmailBody() + "</p>";
	limiter = RateLimiter.create(awsProperties.getMailThrotlePerSecond());
    }

    @Override
    public String sendMail(String toAddress) {

	boolean valid = EmailValidator.getInstance().isValid(toAddress);
	if (!valid) {
	    throw new MsgSenderException("Email address is not valid " + toAddress);
	}
	boolean tryAcquire = limiter.tryAcquire();
	if (!tryAcquire) {
	    throw new MailThrottleException("Mail sending limit reach plz wait for some time");
	}

	String messageId = null;
	try {

	    AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
		    .withCredentials(new InstanceProfileCredentialsProvider(false)).build();
	    SendEmailRequest request = new SendEmailRequest()
		    .withDestination(new Destination().withToAddresses(toAddress))
		    .withMessage(
			    new Message()
				    .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
					    .withText(new Content().withCharset("UTF-8")
						    .withData(awsProperties.getEmailBody())))
				    .withSubject(new Content().withCharset("UTF-8")
					    .withData(awsProperties.getEmailSubject())))
		    .withSource(awsProperties.getFromAddress());
	    SendEmailResult sendEmail = client.sendEmail(request);
	    messageId = sendEmail.getMessageId();
	    LOGGER.info("Message sent to the subscriber {} with details {}", sendEmail, smsAttributes);

	} catch (SdkClientException e) {
	    LOGGER.error("Error while sending the message {}", e);
	    throw new MsgSenderException("Error while sending the message", e);
	}
	return messageId;
    }

}
