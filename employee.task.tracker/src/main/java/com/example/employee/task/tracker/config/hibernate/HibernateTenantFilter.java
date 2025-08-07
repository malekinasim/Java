package com.example.employee.task.tracker.config.hibernate;

import com.example.employee.task.tracker.config.FilterConstants;
import com.example.employee.task.tracker.config.security.JWTTokenProvider;
import com.example.employee.task.tracker.model.Organ;
import com.example.employee.task.tracker.service.organ.OrganService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class HibernateTenantFilter extends OncePerRequestFilter {


    private final EntityManager entityManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final OrganService organService;

    public HibernateTenantFilter(EntityManager entityManager, JWTTokenProvider jwtTokenProvider, OrganService organService) {
        this.entityManager = entityManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.organService = organService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String organCode = resolveTenantId(request);

        if (organCode != null) {
            Organ organ= organService.getOrganByCode(organCode);
            if(organ!=null){
                Session session = entityManager.unwrap(Session.class);
                session.enableFilter(FilterConstants.TENANT_FILTER)
                        .setParameter(FilterConstants.TENANT_FILTER_PARAM, organ.getId());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTenantId(HttpServletRequest request) {
        String organCode = request.getHeader(jwtTokenProvider.CUR_ORGAN_CLAIM);
        if (organCode != null) {
            organCode = jwtTokenProvider.getCurrentOrgan(request);
        }
        return organCode;
    }
}
