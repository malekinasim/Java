package com.example.employee.task.tracker.config.hibernate;

import com.example.employee.task.tracker.config.security.JWTTokenProvider;
import com.example.employee.task.tracker.model.Organ;
import com.example.employee.task.tracker.service.organ.OrganService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class OncePerRequestOrganContextFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;
    private final OrganService organService;

    public OncePerRequestOrganContextFilter(JWTTokenProvider jwtTokenProvider,
                                            OrganService organService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.organService = organService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String organCode = jwtTokenProvider.getCurrentOrgan(request);
            if (organCode != null) {
                Organ organ = organService.getOrganByCode(organCode);
                if (organ != null) {
                    OrganContext.setOrgan(organ);
                }
            }
            filterChain.doFilter(request, response);

        } finally {
            OrganContext.clear();
        }
    }
}

