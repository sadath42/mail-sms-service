package com.arzesh.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.arzesh.model.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleSMSException(SmsException smsException) {
	return new ResponseEntity<ErrorResponse>(new ErrorResponse(smsException.getMessage()),
		HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
