package com.hostfully.dsestaro.entity.booking.controller;

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
import com.hostfully.dsestaro.entity.booking.dto.BookingDto;
import com.hostfully.dsestaro.entity.booking.dto.GuestDto;
import com.hostfully.dsestaro.entity.booking.exception.BookingAlreadyExistsException;
import com.hostfully.dsestaro.entity.booking.exception.BookingDoesntExistsException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidBookingDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidDatesException;
import com.hostfully.dsestaro.entity.booking.exception.InvalidPropertyException;
import com.hostfully.dsestaro.entity.booking.model.Booking;
import com.hostfully.dsestaro.entity.booking.model.BookingStatus;
import com.hostfully.dsestaro.entity.booking.service.BookingService;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTests {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockitoBean
	private BookingService bookingService;

	@Test
	public void testTheBookingCreationWithInconsistentDates() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doThrow(new InvalidBookingDatesException(bookingDto)).when(this.bookingService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testTheBookingCreationWithDatesAlreadyBooked() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doThrow(new InvalidDatesException(bookingDto)).when(this.bookingService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testTheBookingCreationWithBlockAlreadyCreated() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		bookingDto.setBookingId(1);
		
		doThrow(new BookingAlreadyExistsException(bookingDto)).when(this.bookingService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testCreationOfBookingWithoutAnExistingProperty() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doThrow(new InvalidPropertyException(bookingDto)).when(this.bookingService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testCreationOfAValidBooking() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(bookingDto.getStartDate());
			response.setEndDate(bookingDto.getEndDate());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.bookingService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testTheBookingUpdateWithDatesAlreadyBooked() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doThrow(new InvalidBookingDatesException(bookingDto)).when(this.bookingService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testTheBookingUpdateWithInvalidDates() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doThrow(new InvalidDatesException(bookingDto)).when(this.bookingService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testTheBookingUpdateWithoutInformingId() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doThrow(new BookingAlreadyExistsException(bookingDto)).when(this.bookingService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testTheBookingUpdateWithoutAnExistingProperty() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doThrow(new InvalidPropertyException(bookingDto)).when(this.bookingService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testTheBookingUpdateWithValidData() throws Exception {
		BookingDto bookingDto = new BookingDto();
		
		bookingDto.setBookingId(1);
		bookingDto.setStartDate(new Date());
		bookingDto.setEndDate(new Date());
		bookingDto.setGuestList(new ArrayList<GuestDto>());
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(bookingDto.getStartDate());
			response.setEndDate(bookingDto.getEndDate());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.bookingService).update(any());
		
		MockHttpServletRequestBuilder requestBuilder = put("/booking")
                .content(convertObjectToJsonString(bookingDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testTheBookingRetrieving() throws Exception {
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(new Date());
			response.setEndDate(new Date());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.bookingService).find(1);
		
		MockHttpServletRequestBuilder requestBuilder = get("/booking/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testTheBookingRetrievingWithAnInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.bookingService).find(1);
		
		MockHttpServletRequestBuilder requestBuilder = get("/booking/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testCancellingBookingWithValidId() throws Exception {
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(new Date());
			response.setEndDate(new Date());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.CANCELLED);
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.bookingService).cancel(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/booking/1/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testCancellingBookingWithInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.bookingService).cancel(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/booking/1/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testRebookingBookingWithValidId() throws Exception {
		
		doAnswer((Answer<BookingDto>) invocation -> {
			
			BookingDto response = new BookingDto();
			
			response.setStartDate(new Date());
			response.setEndDate(new Date());
			response.setBookingId(1);
			response.setBookingStatus(BookingStatus.BOOKED);
			response.setGuestList(new ArrayList<GuestDto>());
			
			return response;	
		}).when(this.bookingService).rebook(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/booking/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testRebookingBookingWithInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.bookingService).rebook(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/booking/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
	}
	
	@Test
	public void testRebookingBookingWithDatesAlreadyBooked() throws Exception {
		
		Booking booking = new Booking();
		
		booking.setStartDate(new Date());
		booking.setEndDate(new Date());
		
		doThrow(new InvalidBookingDatesException(booking)).when(this.bookingService).rebook(1);
		
		MockHttpServletRequestBuilder requestBuilder = patch("/booking/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict());
	}
	
	@Test
	public void testDeletingBookingWithValidId() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = patch("/booking/1/rebook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testDeletingBookingWithInvalidId() throws Exception {
		
		doThrow(new BookingDoesntExistsException(1)).when(this.bookingService).delete(1);
		
		MockHttpServletRequestBuilder requestBuilder = delete("/booking/1")
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
