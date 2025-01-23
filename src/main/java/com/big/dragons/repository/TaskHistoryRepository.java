package com.big.dragons.repository;

import com.big.dragons.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

    @Query("SELECT t.probability, " +
            "AVG(CASE WHEN t.success = true THEN 1.0 ELSE 0.0 END) " +
            "FROM TaskHistory t GROUP BY t.probability")
    List<Object[]> calculateSuccessRates();
}
