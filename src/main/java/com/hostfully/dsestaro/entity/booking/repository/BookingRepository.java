package com.hostfully.dsestaro.entity.booking.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hostfully.dsestaro.entity.booking.model.Booking;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
	
	@Query("SELECT b FROM Booking b WHERE b.property.id = ?1"
			+ " AND b.startDate <= ?3"
			+ " AND b.endDate >= ?2"
			+ " AND b.bookingStatus = 'BOOKED'")
	Iterable<Booking> findInvalidBookings(long propertyId, Date startDate, Date endDate);
	
	@Query("SELECT b FROM Booking b WHERE b.property.id = ?1"
			+ " AND b.startDate <= ?3"
			+ " AND b.endDate >= ?2"
			+ " AND b.bookingStatus = 'BOOKED'"
			+ " AND b.id != ?4")
	Iterable<Booking> findInvalidBookings(long propertyId, Date startDate, Date endDate, long bookingId);

}