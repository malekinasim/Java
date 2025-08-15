package com.common.config.hibernate;

import com.common.config.Context.RequestContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantFilterAspect {

    @PersistenceContext
    private final  EntityManager entityManager;

    public TenantFilterAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before("execution(* com.common.repository.BaseRepository+.*(..))")
    public void applyOrganFilter() {
         var tenantID = RequestContext.getTenant();
        if (tenantID != null) {
            Session session = entityManager.unwrap(Session.class);
            if (session.getEnabledFilter(FilterConstants.ORGAN_FILTER) == null) {
                session.enableFilter(FilterConstants.ORGAN_FILTER)
                        .setParameter(FilterConstants.ORGAN_FILTER_PARAM, tenantID);
            }
        }
    }
}
