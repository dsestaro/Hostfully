package com.hostfully.dsestaro.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hostfully.dsestaro.entity.booking.exception.BookingDoesntExistsException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidPropertyException;

@ControllerAdvice
public class NotFoundAdvice {

	Logger logger = LoggerFactory.getLogger(NotFoundAdvice.class);
	
	@ResponseStatus(NOT_FOUND)
	@ResponseBody
	@ExceptionHandler(BookingDoesntExistsException.class)
	public String methodBookingDoesntExistsException(BookingDoesntExistsException ex) {

		logger.error(ex.getMessage());
		
		return ex.getMessage();
	}
	
	@ResponseStatus(NOT_FOUND)
	@ResponseBody
	@ExceptionHandler(InvalidPropertyException.class)
	public String methodInvalidPropertyException(InvalidPropertyException ex) {
		
		logger.error(ex.getMessage());
		
		return ex.getMessage();
	}
}
