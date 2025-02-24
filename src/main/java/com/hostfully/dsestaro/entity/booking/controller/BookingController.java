package com.hostfully.dsestaro.entity.booking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostfully.dsestaro.entity.booking.dto.BookingDto;
import com.hostfully.dsestaro.entity.booking.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	
	Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDto booking){

		logger.info("Creating booking for property {}, with start date of {} and end date of {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate());
		
		booking = bookingService.create(booking);
		
		logger.info("Booking for property {}, with start date of {} and end date of {} created successfully with id {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate(),
				booking.getBookingId());
		
        return booking;
    }
	
	@PutMapping
    public BookingDto updateBooking(@Valid @RequestBody BookingDto booking){

		logger.info("Updating booking for property {}, with start date of {} and end date of {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate());
		
		booking = bookingService.update(booking);
		
		logger.info("Updating for property {}, with start date of {} and end date of {} executed successfully with id {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate(),
				booking.getBookingId());
		
        return booking;
    }
	
	@GetMapping(path = "/{id}")
    public BookingDto getBooking(@PathVariable long id){

		logger.info("Retrieving booking with id %s.", 
				id);
		
		 BookingDto booking = bookingService.find(id);
		
		logger.info("Booking with id %s retrieved.", 
				id);
		
        return booking;
    }
	
	@PatchMapping(path = "/{id}/cancel")
    public BookingDto cancelBooking(@PathVariable long id){

		logger.info("Cancelling booking with id %s.", 
				id);
		
		 BookingDto booking = bookingService.cancel(id);
		
		logger.info("Booking with id %s cancelled.", 
				id);
		
        return booking;
    }
	
	@PatchMapping(path = "/{id}/rebook")
    public BookingDto rebookBooking(@PathVariable long id){

		logger.info("Rebooking booking with id %s.", 
				id);
		
		 BookingDto booking = bookingService.rebook(id);
		
		logger.info("Booking with id %s rebooked.", 
				id);
		
        return booking;
    }
	
	@DeleteMapping(path = "/{id}")
    public void deleteBooking(@PathVariable long id){

		logger.info("Deleting booking with id %s.", 
				id);
		
		bookingService.delete(id);
		
		logger.info("Booking with id %s deleted.", 
				id);
    }
}
