package com.hostfully.dsestaro.entity.block.factory;

import java.util.ArrayList;
import java.util.List;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;
import com.hostfully.dsestaro.entity.booking.dto.GuestDto;
import com.hostfully.dsestaro.entity.booking.model.Booking;
import com.hostfully.dsestaro.entity.booking.model.BookingStatus;
import com.hostfully.dsestaro.entity.booking.model.BookingType;
import com.hostfully.dsestaro.entity.booking.model.Guest;
import com.hostfully.dsestaro.entity.property.model.Property;

public class BlockFactory {
	
	public Booking convertForCreation(BookingDto bookingDto, Property property) {
		Booking booking = new Booking();
		
		booking.setProperty(property);
		booking.setStartDate(bookingDto.getStartDate());
		booking.setEndDate(bookingDto.getEndDate());
		booking.setGuestList(convertGuestListDto(bookingDto.getGuestList()));
		booking.setBookingStatus(BookingStatus.BOOKED);
		booking.setBookingType(BookingType.BLOCK);
		
		return booking;
	}

	private List<Guest> convertGuestListDto(List<GuestDto> guestListDto) {
		List<Guest> guests = new ArrayList<Guest>();
		
		for(GuestDto guestDto : guestListDto) {
			Guest guest = new Guest();
			
			guest.setName(guestDto.getName());
			guest.setDocument(guestDto.getDocument());
			
			guests.add(guest);
		}
		
		return guests;
	}
	
	public BookingDto convert(Booking booking) {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(booking.getId());
		bookingDto.setPropertyId(booking.getProperty().getId());
		bookingDto.setStartDate(booking.getStartDate());
		bookingDto.setEndDate(booking.getEndDate());
		bookingDto.setGuestList(convertGuestList(booking.getGuestList()));
		bookingDto.setBookingStatus(booking.getBookingStatus());
		
		return bookingDto;
	}
	
	private List<GuestDto> convertGuestList(List<Guest> guestList) {
		List<GuestDto> guestsDto = new ArrayList<GuestDto>();
		
		for(Guest guest : guestList) {
			GuestDto guestDto = new GuestDto();
			
			guestDto.setName(guest.getName());
			guestDto.setDocument(guest.getDocument());
			
			guestsDto.add(guestDto);
		}
		
		return guestsDto;
	}

	public Booking convertForUpdate(BookingDto booking, Property property, Booking original) {
		
		original.setProperty(property);
		original.setStartDate(booking.getStartDate());
		original.setEndDate(booking.getEndDate());
		original.setGuestList(convertGuestListDto(booking.getGuestList()));
		
		return original;
	}
}
