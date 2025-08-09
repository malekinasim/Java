package com.example.employee.task.tracker.config.hibernate;

import com.example.employee.task.tracker.model.Organ;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

//@Aspect
@Component
public class OrganFilterAspect {

    @PersistenceContext
    private final  EntityManager entityManager;

    public OrganFilterAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before("execution(* com.example.employee.task.tracker.repository..*(..))")
    public void applyTenantFilter() {
        Organ organ = OrganContext.getOrgan();
        if (organ != null) {
            Session session = entityManager.unwrap(Session.class);
            if (session.getEnabledFilter(FilterConstants.ORGAN_FILTER) == null) {
                session.enableFilter(FilterConstants.ORGAN_FILTER)
                        .setParameter(FilterConstants.ORGAN_FILTER_PARAM, organ.getId());
            }
        }
    }
}
