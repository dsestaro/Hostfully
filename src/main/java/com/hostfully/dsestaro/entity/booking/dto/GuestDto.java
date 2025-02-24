package com.hostfully.dsestaro.entity.booking.dto;

import jakarta.validation.constraints.NotNull;

public class GuestDto {

	private int guestId;
	
	@NotNull(message = "Guest name cannot be null.")
	private String name;
	
	@NotNull(message = "Guest document cannot be null.")
	private String document;

	public int getGuestId() {
		return guestId;
	}

	public void setGuestId(int guestId) {
		this.guestId = guestId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
}
