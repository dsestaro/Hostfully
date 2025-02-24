package com.hostfully.dsestaro.entity.block.controller;

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

import com.hostfully.dsestaro.entity.block.service.BlockService;
import com.hostfully.dsestaro.entity.booking.dto.BookingDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/block")
public class BlockController {
	
	@Autowired
	private BlockService blockService;
	
	Logger logger = LoggerFactory.getLogger(BlockController.class);
	
	@PostMapping
    public BookingDto createBlock(@Valid @RequestBody BookingDto booking){

		logger.info("Creating block for property {}, with start date of {} and end date of {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate());
		
		booking = blockService.create(booking);
		
		logger.info("Block for property {}, with start date of {} and end date of {} created successfully with id {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate(),
				booking.getBookingId());
		
        return booking;
    }
	
	@PutMapping
    public BookingDto updateBlock(@Valid @RequestBody BookingDto booking){

		logger.info("Updating block for property {}, with start date of {} and end date of {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate());
		
		booking = blockService.update(booking);
		
		logger.info("Block for property {}, with start date of {} and end date of {} executed successfully with id {}.", 
				booking.getPropertyId(),
				booking.getStartDate(), 
				booking.getEndDate(),
				booking.getBookingId());
		
        return booking;
    }
	
	@GetMapping(path = "/{id}")
    public BookingDto getBlock(@PathVariable long id){

		logger.info("Retrieving block with id %s.", 
				id);
		
		 BookingDto booking = blockService.find(id);
		
		logger.info("Block with id %s retrieved.", 
				id);
		
        return booking;
    }
	
	@PatchMapping(path = "/{id}/cancel")
    public BookingDto cancelBlock(@PathVariable long id){

		logger.info("Cancelling block with id %s.", 
				id);
		
		 BookingDto booking = blockService.cancel(id);
		
		logger.info("Block with id %s cancelled.", 
				id);
		
        return booking;
    }
	
	@PatchMapping(path = "/{id}/rebook")
    public BookingDto RebookBlock(@PathVariable long id){

		logger.info("Rebooking block with id %s.", 
				id);
		
		 BookingDto booking = blockService.rebook(id);
		
		logger.info("Block with id %s rebooked.", 
				id);
		
        return booking;
    }
	
	@DeleteMapping(path = "/{id}")
    public void deleteBlock(@PathVariable long id){

		logger.info("Deleting block with id %s.", 
				id);
		
		blockService.delete(id);
		
		logger.info("Block with id %s deleted.", 
				id);
    }
}
