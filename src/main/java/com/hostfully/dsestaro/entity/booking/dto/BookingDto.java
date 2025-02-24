package com.hostfully.dsestaro.entity.booking.dto;

import java.util.Date;
import java.util.List;

import com.hostfully.dsestaro.entity.booking.model.BookingStatus;

import jakarta.validation.constraints.NotNull;

public class BookingDto {
	
	private long bookingId;
	
	@NotNull(message = "Property id cannot be null.")
	private long propertyId;

	@NotNull(message = "Start date cannot be null.")
	private Date startDate;
	
	@NotNull(message = "End date cannot be null.")
	private Date endDate;
	
	private List<GuestDto> guestList;

	private BookingStatus bookingStatus;
	
	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(long propertyId) {
		this.propertyId = propertyId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<GuestDto> getGuestList() {
		return guestList;
	}

	public void setGuestList(List<GuestDto> guestList) {
		this.guestList = guestList;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
}
