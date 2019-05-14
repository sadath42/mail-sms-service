package com.arzesh.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arzesh.mail.service.MailService;
import com.arzesh.model.MailRequest;
import com.arzesh.sms.service.SMSService;

@RestController
public class MessageController {

    @Autowired
    private SMSService smsService;

    @Autowired
    private MailService mailService;

    @RequestMapping("/sms")
    public ResponseEntity<Map<String, String>> sendSms() {
	String messageId = smsService.sendSMS();
	Map<String, String> map = new HashMap<>();
	map.put("messageId", messageId);
	return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/mail")
    public ResponseEntity<Map<String, String>> senMail(@RequestBody MailRequest mailRequest) {
	String messageId = mailService.sendMail(mailRequest.getToAddress());
	Map<String, String> map = new HashMap<>();
	map.put("messageId", messageId);
	return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
