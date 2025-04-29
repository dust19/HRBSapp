package com.hmsapp.repository;

import com.hmsapp.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {
    @Query("SELECT ra FROM RoomAvailability ra " +
            "WHERE ra.property.id = :propertyId " +
            "AND ra.roomType = :roomType " +
            "AND ra.date BETWEEN :fromDate AND :toDate")
    List<RoomAvailability> findAvailableRooms(
            @Param("propertyId") Long propertyId,
            @Param("roomType") String roomType,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);


}