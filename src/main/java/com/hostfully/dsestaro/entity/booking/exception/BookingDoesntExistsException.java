package com.hostfully.dsestaro.entity.booking.exception;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;

public class BookingDoesntExistsException extends RuntimeException {

    public BookingDoesntExistsException(BookingDto booking) {
        super(String.format("The booking with id %d doesn´t exist.", booking.getBookingId()));
    }
    
    public BookingDoesntExistsException(long bookingId) {
        super(String.format("The booking with id %d doesn´t exist.", bookingId));
    }
}