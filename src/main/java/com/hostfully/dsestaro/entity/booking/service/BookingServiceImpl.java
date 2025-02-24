package com.hostfully.dsestaro.entity.booking.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;
import com.hostfully.dsestaro.entity.booking.exception.BookingAlreadyExistsException;
import com.hostfully.dsestaro.entity.booking.exception.BookingDoesntExistsException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidBookingDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidPropertyException;
import com.hostfully.dsestaro.entity.booking.factory.BookingFactory;
import com.hostfully.dsestaro.entity.booking.model.Booking;
import com.hostfully.dsestaro.entity.booking.model.BookingStatus;
import com.hostfully.dsestaro.entity.booking.model.BookingType;
import com.hostfully.dsestaro.entity.booking.repository.BookingRepository;
import com.hostfully.dsestaro.entity.property.model.Property;
import com.hostfully.dsestaro.entity.property.repository.PropertyRepository;

@Service
public class BookingServiceImpl implements BookingService {

	Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
	
	private BookingRepository bookingRepository;
	private PropertyRepository propertyRepository;
	private BookingFactory bookingFactory;
	
	public BookingServiceImpl(BookingRepository bookingRepository, PropertyRepository propertyRepository, BookingFactory bookingFactory) {
		this.bookingRepository = bookingRepository;
		this.propertyRepository = propertyRepository;
		this.bookingFactory = bookingFactory;
	}

	@Override
	public BookingDto create(BookingDto booking) {
		
		if(!validateDates(booking)) {
			throw new InvalidDatesException(booking);
		}
		
		if(!validateBookingDates(booking)) {
			throw new InvalidBookingDatesException(booking);
		}
		
		if(booking.getBookingId() != 0) {
			throw new BookingAlreadyExistsException(booking);
		}
		
		Optional<Property> property = this.propertyRepository.findById(booking.getPropertyId());
		
		if(property.isEmpty()) {
			throw new InvalidPropertyException(booking);
		}
		
		return this.bookingFactory.convert(
				this.bookingRepository.save(
					this.bookingFactory.convertForCreation(booking, property.get())
				));
		
	}
	
	private boolean validateDates(BookingDto booking) {
		if(booking.getStartDate().after(booking.getEndDate())) {
			return false;
		}
		return true;
	}

	private boolean validateBookingDates(BookingDto booking) {
		
		Iterable<Booking> bookings = this.bookingRepository.findInvalidBookings(
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate());
		
		return bookings.spliterator().getExactSizeIfKnown() == 0;
	}
	
	private boolean validateBookingDates(Booking booking) {
		
		Iterable<Booking> bookings = this.bookingRepository.findInvalidBookings(
				booking.getId(),
				booking.getStartDate(), 
				booking.getEndDate());
		
		return bookings.spliterator().getExactSizeIfKnown() == 0;
	}

	@Override
	public BookingDto update(BookingDto booking) {
		if(!validateDates(booking)) {
			throw new InvalidDatesException(booking);
		}
		
		if(!validateBookingDatesForUpdate(booking)) {
			throw new InvalidBookingDatesException(booking);
		}
		
		Optional<Booking> originalBooking = this.bookingRepository.findById(booking.getBookingId());
		
		if(originalBooking.isEmpty() || originalBooking.get().getBookingType() != BookingType.BOOKING) {
			throw new BookingDoesntExistsException(booking);
		}
		
		Optional<Property> property = this.propertyRepository.findById(booking.getPropertyId());
		
		if(property.isEmpty()) {
			throw new InvalidPropertyException(booking);
		}
		
		return this.bookingFactory.convert(
				this.bookingRepository.save(
					this.bookingFactory.convertForUpdate(booking, property.get(), originalBooking.get())
				));
	}
	
	private boolean validateBookingDatesForUpdate(BookingDto updateBooking) {
		
		Iterable<Booking> bookings = this.bookingRepository.findInvalidBookings(
				updateBooking.getPropertyId(),
				updateBooking.getStartDate(), 
				updateBooking.getEndDate(),
				updateBooking.getBookingId());
		
		return bookings.spliterator().getExactSizeIfKnown() == 0;
	}

	@Override
	public BookingDto find(long bookingId) {
		Optional<Booking> booking = this.bookingRepository.findById(bookingId);
		
		if(booking.isEmpty() || booking.get().getBookingType() != BookingType.BOOKING) {
			throw new BookingDoesntExistsException(bookingId);
		}
		
		return this.bookingFactory.convert(booking.get());
	}

	@Override
	public BookingDto cancel(long bookingId) {
		Optional<Booking> optionalBooking = this.bookingRepository.findById(bookingId);
		
		if(optionalBooking.isEmpty() || optionalBooking.get().getBookingType() != BookingType.BOOKING) {
			throw new BookingDoesntExistsException(bookingId);
		}
		
		Booking booking = optionalBooking.get();
		
		booking.setBookingStatus(BookingStatus.CANCELLED);
		
		return this.bookingFactory.convert(this.bookingRepository.save(booking));
	}

	@Override
	public BookingDto rebook(long bookingId) {
		Optional<Booking> optionalBooking = this.bookingRepository.findById(bookingId);
		
		if(optionalBooking.isEmpty() || optionalBooking.get().getBookingType() != BookingType.BOOKING) {
			throw new BookingDoesntExistsException(bookingId);
		}
		
		Booking booking = optionalBooking.get();
		
		if(!validateBookingDates(booking)) {
			throw new InvalidBookingDatesException(booking);
		}
		
		booking.setBookingStatus(BookingStatus.BOOKED);
		
		return this.bookingFactory.convert(this.bookingRepository.save(booking));
	}

	@Override
	public void delete(long bookingId) {
		Optional<Booking> optionalBooking = this.bookingRepository.findById(bookingId);
		
		if(optionalBooking.isEmpty() || optionalBooking.get().getBookingType() != BookingType.BOOKING) {
			throw new BookingDoesntExistsException(bookingId);
		}
		
		Booking booking = optionalBooking.get();
		
		booking.setProperty(null);
		
		this.bookingRepository.delete(booking);
	}
}
