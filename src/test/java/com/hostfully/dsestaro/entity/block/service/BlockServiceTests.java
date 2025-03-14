package com.hostfully.dsestaro.entity.block.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.hostfully.dsestaro.entity.block.factory.BlockFactory;
import com.hostfully.dsestaro.entity.booking.dto.BookingDto;
import com.hostfully.dsestaro.entity.booking.dto.GuestDto;
import com.hostfully.dsestaro.entity.booking.exception.BookingAlreadyExistsException;
import com.hostfully.dsestaro.entity.booking.exception.BookingDoesntExistsException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidBookingDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidPropertyException;
import com.hostfully.dsestaro.entity.booking.model.Booking;
import com.hostfully.dsestaro.entity.booking.model.BookingStatus;
import com.hostfully.dsestaro.entity.booking.model.BookingType;
import com.hostfully.dsestaro.entity.booking.model.Guest;
import com.hostfully.dsestaro.entity.booking.repository.BookingRepository;
import com.hostfully.dsestaro.entity.property.model.Property;
import com.hostfully.dsestaro.entity.property.repository.PropertyRepository;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTests {

	@Mock
	private BookingRepository bookingRepository;
	
	@Mock
	private PropertyRepository propertyRepository;
	
	@Mock
	private BlockFactory blockFactory;
	
	private BlockService blockService;
	
	@BeforeEach
	public void configure() {
		this.blockService = new BlockServiceImpl(bookingRepository, propertyRepository, blockFactory);
	}
	
	@Test
	public void testInvalidDateRange() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		
		assertThrows(InvalidDatesException.class, () -> this.blockService.create(bookingDto));
	}
	
	@Test
	public void testBookingDatesAlreadyBooked() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		
		doAnswer((Answer<Iterable<Booking>>) invocation -> {
			
			List<Booking> bookings = new ArrayList<Booking>();
			
			bookings.add(new Booking());
			
			return bookings;	
		}).when(this.bookingRepository).findInvalidBookings(bookingDto.getPropertyId(), bookingDto.getStartDate(), bookingDto.getEndDate());
		
		assertThrows(InvalidBookingDatesException.class, () -> this.blockService.create(bookingDto));
	}
	
	@Test
	public void testInvalidBookingId() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		
		doAnswer((Answer<Iterable<Booking>>) invocation -> {
			
			List<Booking> bookings = new ArrayList<Booking>();
			
			return bookings;	
		}).when(this.bookingRepository).findInvalidBookings(bookingDto.getPropertyId(), bookingDto.getStartDate(), bookingDto.getEndDate());
		
		assertThrows(BookingAlreadyExistsException.class, () -> this.blockService.create(bookingDto));
	}
	
	@Test
	public void testInvalidPropertyId() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		
		assertThrows(InvalidPropertyException.class, () -> this.blockService.create(bookingDto));
	}
	
	@Test
	public void testValidBlock() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		
		doAnswer((Answer<Optional<Property>>) invocation -> {
			
			Property property = new Property();
			
			property.setId(1);
			
			return Optional.of(property);	
		}).when(this.propertyRepository).findById(any());
		
		doAnswer((Answer<Booking>) invocation -> {
			
			Booking response = new Booking();
		
			Property property = new Property();
			
			property.setId(1);
			
			response.setProperty(property);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<Guest>());
			
			return response;	
		}).when(this.blockFactory).convertForCreation(any(), any());
		
		doAnswer((Answer<Booking>) invocation -> {
			
			Booking response = new Booking();
		
			Property property = new Property();
			
			property.setId(1);
			
			response.setId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setProperty(property);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<Guest>());
			
			return response;	
		}).when(this.bookingRepository).save(any());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setPropertyId(1);
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.blockFactory).convert(any());
		
		BookingDto response = this.blockService.create(bookingDto);
		
		assertEquals(bookingDto.getStartDate(), response.getStartDate());
		assertEquals(bookingDto.getEndDate(), response.getEndDate());
		assertEquals(1, response.getBookingId());
		assertEquals(BookingStatus.BOOKED, response.getBookingStatus());
	}
	
	@Test
	public void testInvalidDateRangeOnUpdate() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setBookingId(1);
		
		assertThrows(InvalidDatesException.class, () -> this.blockService.update(bookingDto));
	}
	
	@Test
	public void testInvalidBookingDatesOnUpdate() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		bookingDto.setBookingId(1);
		
		doAnswer((Answer<Iterable<Booking>>) invocation -> {
			
			List<Booking> bookings = new ArrayList<Booking>();
			
			bookings.add(new Booking());
			
			return bookings;	
		}).when(this.bookingRepository).findInvalidBookings(bookingDto.getPropertyId(), bookingDto.getStartDate(), bookingDto.getEndDate(), bookingDto.getBookingId());
		
		assertThrows(InvalidBookingDatesException.class, () -> this.blockService.update(bookingDto));
	}
	
	@Test
	public void testInvalidBookingIdOnUpdate() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(0);
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		
		doAnswer((Answer<Iterable<Booking>>) invocation -> {
			
			List<Booking> bookings = new ArrayList<Booking>();
			
			return bookings;	
		}).when(this.bookingRepository).findInvalidBookings(bookingDto.getPropertyId(), bookingDto.getStartDate(), bookingDto.getEndDate(), bookingDto.getBookingId());
		
		assertThrows(BookingDoesntExistsException.class, () -> this.blockService.update(bookingDto));
	}
	
	@Test
	public void testInvalidPropertyIdOnUpdate() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		bookingDto.setBookingId(1);
		
		doAnswer((Answer<Optional<Booking>>) invocation -> {
			
			Booking response = new Booking();
			
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setId(1);
			response.setBookingType(BookingType.BLOCK);
			
			return Optional.of(response);	
		}).when(this.bookingRepository).findById(any());
		
		assertThrows(InvalidPropertyException.class, () -> this.blockService.update(bookingDto));
	}
	
	@Test
	public void testValidBookingOnUpdate() {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setPropertyId(1);
		bookingDto.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
		bookingDto.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doAnswer((Answer<Optional<Booking>>) invocation -> {
			
			Booking response = new Booking();
			
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setId(1);
			response.setBookingType(BookingType.BLOCK);
			
			return Optional.of(response);	
		}).when(this.bookingRepository).findById(any());
		
		doAnswer((Answer<Optional<Property>>) invocation -> {
			
			Property property = new Property();
			
			property.setId(1);
			
			return Optional.of(property);	
		}).when(this.propertyRepository).findById(any());
		
		doAnswer((Answer<Booking>) invocation -> {
			
			Booking response = new Booking();
		
			Property property = new Property();
			
			property.setId(1);
			
			response.setProperty(property);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<Guest>());
			
			return response;	
		}).when(this.blockFactory).convertForUpdate(any(), any(), any());
		
		doAnswer((Answer<Booking>) invocation -> {
			
			Booking response = new Booking();
		
			Property property = new Property();
			
			property.setId(1);
			
			response.setId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setProperty(property);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<Guest>());
			
			return response;	
		}).when(this.bookingRepository).save(any());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setPropertyId(1);
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.blockFactory).convert(any());
		
		BookingDto response = this.blockService.update(bookingDto);
		
		assertEquals(bookingDto.getStartDate(), response.getStartDate());
		assertEquals(bookingDto.getEndDate(), response.getEndDate());
		assertEquals(1, response.getBookingId());
		assertEquals(BookingStatus.BOOKED, response.getBookingStatus());
	}
	
	@Test
	public void testInvalidBookingIdOnRetrieve() {
		assertThrows(BookingDoesntExistsException.class, () -> this.blockService.find(1));
	}
	
	@Test
	public void testValidBookingIdOnRetrieve() {
		
		doAnswer((Answer<Optional<Booking>>) invocation -> {
			
			Booking response = new Booking();
			
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setId(1);
			response.setBookingType(BookingType.BLOCK);
			
			return Optional.of(response);	
		}).when(this.bookingRepository).findById(any());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setPropertyId(1);
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.blockFactory).convert(any());
		
		BookingDto response = this.blockService.find(1);
		
		assertEquals(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime(), response.getStartDate());
		assertEquals(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime(), response.getEndDate());
		assertEquals(1, response.getBookingId());
		assertEquals(BookingStatus.BOOKED, response.getBookingStatus());
	}
	
	@Test
	public void testInvalidBookingIdOnCancellation() {
		assertThrows(BookingDoesntExistsException.class, () -> this.blockService.cancel(1));
	}
	
	@Test
	public void testValidBookingOnCancellation() {
		
		doAnswer((Answer<Optional<Booking>>) invocation -> {
			
			Booking response = new Booking();
			
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setId(1);
			response.setBookingType(BookingType.BLOCK);
			
			return Optional.of(response);	
		}).when(this.bookingRepository).findById(any());
		
		doAnswer((Answer<Booking>) invocation -> {
			
			Booking response = new Booking();
		
			Property property = new Property();
			
			property.setId(1);
			
			response.setId(1);
			response.setBookingStatus(BookingStatus.CANCELLED);
			response.setProperty(property);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<Guest>());
			
			return response;	
		}).when(this.bookingRepository).save(any());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setPropertyId(1);
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.CANCELLED);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.blockFactory).convert(any());
		
		BookingDto response = this.blockService.cancel(1);
		
		assertEquals(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime(), response.getStartDate());
		assertEquals(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime(), response.getEndDate());
		assertEquals(1, response.getBookingId());
		assertEquals(BookingStatus.CANCELLED, response.getBookingStatus());
	}
	
	@Test
	public void testInvalidBookingIdOnRebooking() {
		assertThrows(BookingDoesntExistsException.class, () -> this.blockService.rebook(1));
	}
	
	@Test
	public void testInvalidBookingDatesOnRebooking() {
		
		doAnswer((Answer<Optional<Booking>>) invocation -> {
			
			Booking response = new Booking();
			
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setId(1);
			response.setBookingType(BookingType.BLOCK);
			
			return Optional.of(response);	
		}).when(this.bookingRepository).findById(any());
		
		doAnswer((Answer<Iterable<Booking>>) invocation -> {
			
			List<Booking> bookings = new ArrayList<Booking>();
			
			bookings.add(new Booking());
			
			return bookings;	
		}).when(this.bookingRepository).findInvalidBookings(1, 
				new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime(), 
				new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
		
		assertThrows(InvalidBookingDatesException.class, () -> this.blockService.rebook(1));
	}
	
	@Test
	public void testValidBookingOnRebooking() {
		
		doAnswer((Answer<Optional<Booking>>) invocation -> {
			
			Booking response = new Booking();
			
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setId(1);
			response.setBookingType(BookingType.BLOCK);
			
			return Optional.of(response);	
		}).when(this.bookingRepository).findById(any());
		
		doAnswer((Answer<Booking>) invocation -> {
			
			Booking response = new Booking();
		
			Property property = new Property();
			
			property.setId(1);
			
			response.setId(1);
			response.setBookingStatus(BookingStatus.CANCELLED);
			response.setProperty(property);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<Guest>());
			
			return response;	
		}).when(this.bookingRepository).save(any());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setPropertyId(1);
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.blockFactory).convert(any());
		
		BookingDto response = this.blockService.rebook(1);
		
		assertEquals(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime(), response.getStartDate());
		assertEquals(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime(), response.getEndDate());
		assertEquals(1, response.getBookingId());
		assertEquals(BookingStatus.BOOKED, response.getBookingStatus());
	}
	
	@Test
	public void testInvalidBookingIdOnDeletion() {
		assertThrows(BookingDoesntExistsException.class, () -> this.blockService.delete(1));
	}
	
	@Test
	public void testValidBookingOnDeletion() {
		
		doAnswer((Answer<Optional<Booking>>) invocation -> {
			
			Booking response = new Booking();
			
			response.setStartDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 1).getTime());
			response.setEndDate(new GregorianCalendar(2025, Calendar.FEBRUARY, 10).getTime());
			response.setId(1);
			response.setBookingType(BookingType.BLOCK);
			
			return Optional.of(response);	
		}).when(this.bookingRepository).findById(any());
		
		this.blockService.delete(1);
	}
}
