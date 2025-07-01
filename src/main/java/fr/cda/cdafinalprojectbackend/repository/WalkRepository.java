package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.dto.walk.WalkLocationDTO;
import fr.cda.cdafinalprojectbackend.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalkRepository extends JpaRepository<Walk, Long> {
    @Query(value =
            "SELECT w.id, w.start_latitude as latitude, w.start_longitude as longitude, w.created_by as createdBy " +
            "FROM walk w " +
            "WHERE " +
            "  CAST(w.start_latitude AS double precision) BETWEEN (:latitude - (:radius / 111.0)) AND (:latitude + (:radius / 111.0)) " +
            "  AND CAST(w.start_longitude AS double precision) BETWEEN (:longitude - (:radius / (111.0 * COS(RADIANS(:latitude))))) AND (:longitude + (:radius / (111.0 * COS(RADIANS(:latitude))))) " +
            "  AND (6371 * acos(cos(radians(:latitude)) * cos(radians(CAST(w.start_latitude AS double precision))) * " +
            "  cos(radians(CAST(w.start_longitude AS double precision)) - radians(:longitude)) + " +
            "  sin(radians(:latitude)) * sin(radians(CAST(w.start_latitude AS double precision))))) <= :radius " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(CAST(w.start_latitude AS double precision))) * " +
            "  cos(radians(CAST(w.start_longitude AS double precision)) - radians(:longitude)) + " +
            "  sin(radians(:latitude)) * sin(radians(CAST(w.start_latitude AS double precision)))))"
            ,nativeQuery = true)
    List<WalkLocationDTO> findWalksCloseToUser(double latitude, double longitude, double radius);
}