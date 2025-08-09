package com.example.employee.task.tracker.repository.task;

import com.example.employee.task.tracker.model.TaskHistory;
import com.example.employee.task.tracker.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaskHistoryRepository extends BaseRepository<TaskHistory, Long> {
    @Query(value = "select  th from TaskHistory th " +
            " inner join th.task t where t.id=:taskId  " +
            "order by th.fromDate asc limit 1")
    Optional<TaskHistory> getLastTaskStatus(Long taskId);
}
