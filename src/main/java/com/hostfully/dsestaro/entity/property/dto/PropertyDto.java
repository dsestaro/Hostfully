package com.hostfully.dsestaro.entity.property.dto;

import jakarta.validation.constraints.NotNull;

public class PropertyDto {
	
	@NotNull(message = "Property name cannot be null.")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
