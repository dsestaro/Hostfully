package com.hostfully.dsestaro.entity.property.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.hostfully.dsestaro.entity.property.dto.PropertyDto;
import com.hostfully.dsestaro.entity.property.factory.PropertyFactory;
import com.hostfully.dsestaro.entity.property.model.Property;
import com.hostfully.dsestaro.entity.property.repository.PropertyRepository;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTests {
	
	@Mock
	private PropertyRepository propertyRepository;
	
	@Mock
	private PropertyFactory propertyFactory;
	
	private PropertyService propertyService;
	
	@BeforeEach
	public void configure() {
		this.propertyService = new PropertyServiceImpl(propertyRepository, propertyFactory);
	}
	
	@Test
	public void testCreateProperty() {
		PropertyDto request = new PropertyDto();
		
		request.setName("Create");
		
		doAnswer((Answer<Property>) invocation -> {
			
			Property response = new Property();
			
			response.setName(request.getName());
			
			return response;	
		}).when(this.propertyFactory).convert(any(PropertyDto.class));
		
		doAnswer((Answer<PropertyDto>) invocation -> {
			
			PropertyDto response = new PropertyDto();
			
			response.setName(request.getName());
			
			return response;	
		}).when(this.propertyFactory).convert(any(Property.class));
		
		doAnswer((Answer<Property>) invocation -> {
			
			Property response = new Property();
			
			response.setName(request.getName());
			response.setId(1);
			response.setVersion(1);
			
			return response;	
		}).when(this.propertyRepository).save(any(Property.class));
		
		PropertyDto response = this.propertyService.create(request);
		
		assertEquals(request.getName(), response.getName());
	}
}
