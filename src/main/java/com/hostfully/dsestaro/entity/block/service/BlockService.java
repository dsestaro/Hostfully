package com.hostfully.dsestaro.entity.block.service;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;

public interface BlockService {
	
	BookingDto create(BookingDto booking);

	BookingDto update(BookingDto booking);

	BookingDto find(long bookingId);

	BookingDto cancel(long bookingId);

	BookingDto rebook(long bookingId);

	void delete(long bookingId);
}
