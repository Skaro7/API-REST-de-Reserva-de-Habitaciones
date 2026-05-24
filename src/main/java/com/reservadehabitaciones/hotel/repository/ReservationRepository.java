package com.reservadehabitaciones.hotel.repository;

import com.reservadehabitaciones.hotel.entity.Reservation;
import com.reservadehabitaciones.hotel.entity.ReservationStatus;
import com.reservadehabitaciones.hotel.entity.Room;
import com.reservadehabitaciones.hotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByUserAndStatus(User user, ReservationStatus status);
    List<Reservation> findByRoomAndStatus(Room room, ReservationStatus status);

    @Query("SELECT r from Reservation r WHERE r.user = :user AND r.status = 'ACTIVE' AND r.startDate " +
            "< :endDate AND r.endDate > :startDate")
    List<Reservation> findConflictingReservationsForUser(
            @Param("user") User user,
            @Param("startDate") java.time.LocalDate startDate,
            @Param("endDate") java.time.LocalDate endDate
    );

    @Query("SELECT r from Reservation r WHERE r.room = :room AND r.status = 'ACTIVE' AND r.startDate " +
            "< :endDate AND r.endDate > :startDate")
    List<Reservation> findConflictingReservationsForRoom(
            @Param("room") Room room,
            @Param("startDate") java.time.LocalDate startDate,
            @Param("endDate") java.time.LocalDate endDate
    );
}
