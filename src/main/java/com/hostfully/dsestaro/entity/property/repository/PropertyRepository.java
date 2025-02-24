package com.hostfully.dsestaro.entity.property.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hostfully.dsestaro.entity.property.model.Property;

@Repository	
public interface PropertyRepository extends CrudRepository<Property, Long> {

}
