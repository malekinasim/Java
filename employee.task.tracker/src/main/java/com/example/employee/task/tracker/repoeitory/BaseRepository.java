package com.example.employee.task.tracker.repoeitory;

import com.example.employee.task.tracker.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepository<T, ID> extends JpaRepository<T,ID> {
}
