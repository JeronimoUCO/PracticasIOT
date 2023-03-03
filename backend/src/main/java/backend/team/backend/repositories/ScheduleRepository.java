package backend.team.backend.repositories;


import backend.team.backend.models.ScheduleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleModel,Long > {
    ScheduleModel findScheduleById(long id);

    @Query(value = "SELECT * FROM agenda WHERE DATE >= :startDate AND DATE <= :endDate", nativeQuery = true)
    List<ScheduleModel> findScheduleWithinDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    List<ScheduleModel> findScheduleByDate(LocalDateTime date);
    List<ScheduleModel> findAllByWarehouseId(Integer id);
}

