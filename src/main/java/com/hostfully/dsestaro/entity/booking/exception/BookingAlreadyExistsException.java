package com.hostfully.dsestaro.entity.booking.exception;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;

public class BookingAlreadyExistsException extends RuntimeException {

    public BookingAlreadyExistsException(BookingDto booking) {
        super(String.format("The booking with id %d already exist, use the PUT method to update it´s content.", booking.getBookingId()));
    }
}
