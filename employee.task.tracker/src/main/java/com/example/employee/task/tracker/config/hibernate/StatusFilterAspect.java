package com.example.employee.task.tracker.config.hibernate;

import com.example.employee.task.tracker.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class StatusFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Around("@annotation(com.example.employee.task.tracker.config.hibernate.StatusFilter)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // Extract annotation and its parameter
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        StatusFilter annotation = method.getAnnotation(StatusFilter.class);
        BaseEntity.Status statusValue = annotation.status();

        // Enable filter
        Session session = entityManager.unwrap(Session.class);
        boolean enabledHere = false;

        if (session.getEnabledFilter(FilterConstants.STATUS_FILTER) == null) {

            session.enableFilter(FilterConstants.STATUS_FILTER)
                    .setParameter(FilterConstants.STATUS_FILTER_PARAM,statusValue);
            enabledHere = true;
        }

        try {
            return pjp.proceed();
        } finally {
            if (enabledHere) {
                session.disableFilter(FilterConstants.STATUS_FILTER);
            }
        }
    }
}

