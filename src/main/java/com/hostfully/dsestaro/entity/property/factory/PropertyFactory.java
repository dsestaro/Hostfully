package com.hostfully.dsestaro.entity.property.factory;

import org.springframework.stereotype.Service;

import com.hostfully.dsestaro.entity.property.dto.PropertyDto;
import com.hostfully.dsestaro.entity.property.model.Property;

@Service
public class PropertyFactory {

	public Property convert(PropertyDto propertyDto) {
		Property property = new Property();
		
		property.setName(propertyDto.getName());
		
		return property;
	}
	
	public PropertyDto convert(Property property) {
		PropertyDto propertyDto = new PropertyDto();
		
		propertyDto.setName(property.getName());
		
		return propertyDto;
	}
}
