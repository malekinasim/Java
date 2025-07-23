package com.example.employee.task.tracker.service.authprovider;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.service.BaseService;



public interface AuthProviderService extends BaseService<AuthProvider,Long> {
    AuthProvider findByName(String authProvider);
}
