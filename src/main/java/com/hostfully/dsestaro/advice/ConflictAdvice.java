package com.hostfully.dsestaro.advice;

import static org.springframework.http.HttpStatus.CONFLICT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hostfully.dsestaro.entity.booking.exception.BookingAlreadyExistsException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidBookingDatesException;;

@ControllerAdvice
public class ConflictAdvice {

	Logger logger = LoggerFactory.getLogger(ConflictAdvice.class);
	
	@ResponseStatus(CONFLICT)
	@ResponseBody
	@ExceptionHandler(InvalidBookingDatesException.class)
	public String methodInvalidBookingDatesException(InvalidBookingDatesException ex) {

		logger.error(ex.getMessage());
		
		return ex.getMessage();
	}
	
	@ResponseStatus(CONFLICT)
	@ResponseBody
	@ExceptionHandler(BookingAlreadyExistsException.class)
	public String methodBookingAlreadyExistsException(BookingAlreadyExistsException ex) {

		logger.error(ex.getMessage());
		
		return ex.getMessage();
	}
}
