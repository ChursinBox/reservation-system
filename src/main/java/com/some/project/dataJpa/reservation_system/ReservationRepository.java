package com.some.project.dataJpa.reservation_system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

//    @Query("SELECT r from ReservationEntity r where r.status = :status")
//    List<ReservationEntity> findAllByStatus(ReservationStatus status);
//
//    @Query("SELECT r from ReservationEntity r where r.roomId = :roomId")
//    List<ReservationEntity> findAllByRoomId(@Param("room id") Long roomId);
//
//    @Transactional
//    @Modifying
//    @Query("""
//            update ReservationEntity r
//                    set r.userId = :userId,
//                        r.roomId = :roomId,
//                        r.startDate = :startDate,
//                        r.endDate = :endDate,
//                        r.status = :status
//                    where r.id =: id
//            """)
//    int updateAllFields(
//            @Param("id") Long id,
//            @Param("userId") Long userId,
//            @Param("roomId") Long roomId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("status") ReservationStatus status
//    );

    @Modifying
    @Query("""
            update ReservationEntity r
                    set r.status = :status
                    where r.id = :id
            """)
    void setStatus(
            @Param("id") Long id,
            @Param("status") ReservationStatus reservationStatus);
}
