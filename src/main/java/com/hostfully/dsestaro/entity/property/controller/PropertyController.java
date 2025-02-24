package com.hostfully.dsestaro.entity.property.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostfully.dsestaro.entity.property.dto.PropertyDto;
import com.hostfully.dsestaro.entity.property.service.PropertyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/property")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;
	
	Logger logger = LoggerFactory.getLogger(PropertyController.class);
	
	@PostMapping
    public PropertyDto createProperty(@Valid @RequestBody PropertyDto property){

		logger.info("Creating property with name {}.", property.getName());
		
		PropertyDto propertyDto = propertyService.create(property);
		
		logger.info("Property with name {} created successfully.", property.getName());
		
        return propertyDto;
    }
}
