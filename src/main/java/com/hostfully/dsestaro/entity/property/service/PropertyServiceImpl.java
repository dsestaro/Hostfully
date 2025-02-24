package com.hostfully.dsestaro.entity.property.service;

import org.springframework.stereotype.Service;

import com.hostfully.dsestaro.entity.property.dto.PropertyDto;
import com.hostfully.dsestaro.entity.property.factory.PropertyFactory;
import com.hostfully.dsestaro.entity.property.repository.PropertyRepository;

@Service
public class PropertyServiceImpl implements PropertyService {

	private final PropertyRepository propertyRepository;
	private final PropertyFactory propertyFactory;
	
	public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyFactory propertyFactory2) {
		this.propertyRepository = propertyRepository;
		this.propertyFactory = propertyFactory2;
	}

	@Override
	public PropertyDto create(PropertyDto property) {
		
		return this.propertyFactory.convert(
				this.propertyRepository.save(this.propertyFactory.convert(property))
		);
	}

}
