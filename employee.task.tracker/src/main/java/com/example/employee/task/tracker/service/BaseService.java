package com.example.employee.task.tracker.service;

import com.example.employee.task.tracker.model.BaseEntity;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import java.util.List;
import java.util.Optional;

public interface BaseService<T,ID,R extends BaseRepository<T,ID>>{
    R getRepository();
    T findById(ID id);
    void deleteById(ID id);
    List<T> findAll();
    T save(T t);
    T update(T t);
}
