package com.hostfully.dsestaro.entity.booking.exception;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;
import com.hostfully.dsestaro.entity.booking.model.Booking;

public class InvalidBookingDatesException extends RuntimeException {

	public InvalidBookingDatesException(BookingDto booking) {
		super(String.format("The period between %s and %s is not available for booking.", booking.getStartDate(),
				booking.getEndDate()));
	}

	public InvalidBookingDatesException(Booking booking) {
		super(String.format("The period between %s and %s is not available for booking.", booking.getStartDate(),
				booking.getEndDate()));
	}
}
