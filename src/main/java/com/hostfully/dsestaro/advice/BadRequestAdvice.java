package com.hostfully.dsestaro.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hostfully.dsestaro.entity.booking.exception.InvalidDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidPropertyException;

@ControllerAdvice
public class BadRequestAdvice {
	
	Logger logger = LoggerFactory.getLogger(BadRequestAdvice.class);
	
	@ResponseStatus(BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(InvalidDatesException.class)
	public String methodInvalidDatesException(InvalidDatesException ex) {
		
		logger.error(ex.getMessage());
		
		return ex.getMessage();
	}
}
