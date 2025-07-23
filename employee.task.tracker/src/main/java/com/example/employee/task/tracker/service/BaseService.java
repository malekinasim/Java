package com.example.employee.task.tracker.service;

import java.util.List;

public interface BaseService<T,ID>{
    T findById(ID id);
    void deleteById(ID id);
    List<T> findAll();
    T save(T t);
    T update(T t);

}
