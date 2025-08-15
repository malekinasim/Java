package com.common.config.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StatusFilterAspect {
    @PersistenceContext
    private final  EntityManager entityManager;

    public StatusFilterAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before("execution(* com.common.repository.BaseRepository+.*(..))")
    public void applyTenantFilter() {
        Session session = entityManager.unwrap(Session.class);
        if (session.getEnabledFilter(FilterConstants.STATUS_FILTER) == null) {
            session.enableFilter(FilterConstants.STATUS_FILTER);
        }
    }
}
