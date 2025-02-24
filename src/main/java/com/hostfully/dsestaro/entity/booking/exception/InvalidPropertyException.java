package com.hostfully.dsestaro.entity.booking.exception;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;

public class InvalidPropertyException extends RuntimeException {

	public InvalidPropertyException(BookingDto booking) {
		super(String.format("Property with id %s doesn´t exist.", booking.getPropertyId()));
	}
}
