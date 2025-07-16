package com.example.employee.task.tracker.config.aop;

import com.example.employee.task.tracker.config.FilterConstants;
import com.example.employee.task.tracker.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EnableStatusFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* com.example.employee.task.tracker.service..*(..))")
    public void enableStatusFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(FilterConstants.STATUS_FILTER).setParameter("status", BaseEntity.Status.ACTIVE);
    }
}
