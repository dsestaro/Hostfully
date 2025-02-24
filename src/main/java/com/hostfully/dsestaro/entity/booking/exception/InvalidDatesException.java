package com.hostfully.dsestaro.entity.booking.exception;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;

public class InvalidDatesException extends RuntimeException {

	public InvalidDatesException(BookingDto booking) {
		super(String.format("Invalid data range for booking with property id %d and start date %s and end date %s.",
				booking.getBookingId(), booking.getStartDate(), booking.getEndDate()));
	}
}
