package com.hostfully.dsestaro.entity.property.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.hostfully.dsestaro.entity.property.dto.PropertyDto;
import com.hostfully.dsestaro.entity.property.service.PropertyService;

@SpringBootTest
@AutoConfigureMockMvc
public class PropertyControllerTests {

	@Autowired
    private MockMvc mockMvc;
	
	@MockitoBean
	private PropertyService propertyService;
	
	@Test
	public void testCreationOfPropertyWithValidInformation() throws Exception {
		PropertyDto propertyDto = new PropertyDto();
		
		propertyDto.setName("Unity test");
		
		doAnswer((Answer<PropertyDto>) invocation -> {
			
			PropertyDto response = new PropertyDto();
			
			response.setName(propertyDto.getName());
			
			return response;	
		}).when(this.propertyService).create(any());
		
		MockHttpServletRequestBuilder requestBuilder = post("/property")
                .content(convertObjectToJsonString(propertyDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
	}
	
	@Test
	public void testCreationOfPropertyWithoutName() throws Exception {
		PropertyDto propertyDto = new PropertyDto();
		
		MockHttpServletRequestBuilder requestBuilder = post("/property")
                .content(convertObjectToJsonString(propertyDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder)
        	.andExpect(status().isBadRequest());
	}
	
	private String convertObjectToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
