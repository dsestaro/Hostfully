package com.hostfully.dsestaro.entity.block.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostfully.dsestaro.entity.block.service.BlockService;
import com.hostfully.dsestaro.entity.booking.dto.BookingDto;
import com.hostfully.dsestaro.entity.booking.dto.GuestDto;
import com.hostfully.dsestaro.entity.booking.exception.BookingAlreadyExistsException;
import com.hostfully.dsestaro.entity.booking.exception.BookingDoesntExistsException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidBookingDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidPropertyException;
import com.hostfully.dsestaro.entity.booking.model.Booking;
import com.hostfully.dsestaro.entity.booking.model.BookingStatus;

@SpringBootTest
@AutoConfigureMockMvc
public class BlockControllerTests {

	@Autowired
    private MockMvc mockMvc;
	
	@MockitoBean
	private BlockService blockService;

	@Test
	public void testTheBlockCreationWithInconsistentDates() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doThrow(new InvalidBookingDatesException(bookingDto)).when(this.blockService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testTheBlockCreationWithDatesAlreadyBooked() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doThrow(new InvalidDatesException(bookingDto)).when(this.blockService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testTheBlockCreationWithBlockAlreadyCreated() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setBookingId(1);
		
		doThrow(new BookingAlreadyExistsException(bookingDto)).when(this.blockService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testCreationOfBlockWithoutAnExistingProperty() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doThrow(new InvalidPropertyException(bookingDto)).when(this.blockService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testCreationOfAValidBlock() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(bookingDto.getStartDate());
			response.setEndDate(bookingDto.getEndDate());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			
			return response;	
		}).when(this.blockService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testTheBlockUpdateWithDatesAlreadyBooked() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doThrow(new InvalidBookingDatesException(bookingDto)).when(this.blockService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testTheBlockUpdateWithInvalidDates() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doThrow(new InvalidDatesException(bookingDto)).when(this.blockService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testTheBlockUpdateWithoutInformingId() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doThrow(new BookingAlreadyExistsException(bookingDto)).when(this.blockService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testTheBlockUpdateWithoutAnExistingProperty() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doThrow(new InvalidPropertyException(bookingDto)).when(this.blockService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testTheBlockUpdateWithValidData() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(bookingDto.getStartDate());
			response.setEndDate(bookingDto.getEndDate());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.blockService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/block")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testTheBlockRetrieving() throws Exception {
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(new Date());
			response.setEndDate(new Date());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			
			return response;	
		}).when(this.blockService).find(1);
		
		MockHttpServletRequestBuilder requestBuilder = get("/block/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testTheBlockRetrievingWithAnInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.blockService).find(1);
		
		MockHttpServletRequestBuilder requestBuilder = get("/block/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testCancellingBlockWithValidId() throws Exception {
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(new Date());
			response.setEndDate(new Date());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.CANCELLED);
			
			return response;	
		}).when(this.blockService).cancel(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/block/1/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testCancellingBlockWithInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.blockService).cancel(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/block/1/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testRebookingBlockWithValidId() throws Exception {
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(new Date());
			response.setEndDate(new Date());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			
			return response;	
		}).when(this.blockService).rebook(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/block/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testRebookingBlockWithInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.blockService).rebook(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/block/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testRebookingBlockWithDatesAlreadyBooked() throws Exception {
		
		Booking booking = new Booking();
		
		booking.setStartDate(new Date());
		booking.setEndDate(new Date());
		
		doThrow(new InvalidBookingDatesException(booking)).when(this.blockService).rebook(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/block/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testDeletingBlockWithValidId() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = patch("/block/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testDeletingBlockWithInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.blockService).delete(1);
		
		MockHttpServletRequestBuilder requestBuilder = delete("/block/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	private String convertObjectToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
