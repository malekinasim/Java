package com.example.employee.task.tracker.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private final CustomUserDetailsService customUserDetailsService;

    public JWTAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        tokenProvider.resolveToken(request);
        try {
            String jwtToken = tokenProvider.resolveToken(request);
            if (StringUtils.hasText(jwtToken)
                    && tokenProvider.validateToken(jwtToken)) {
                Claims claims = tokenProvider.getClaims(jwtToken);
                Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
                if (existingAuth == null || !existingAuth.isAuthenticated()) {
                    Authentication authentication = setAuthentication(claims, request, jwtToken);
                    if (authentication == null) {
                        SecurityContextHolder.clearContext();
                    } else {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            if (!request.getRequestURI().equals("/auth/refresh-token")
                    && !request.getRequestURI().equals("/auth/logoff")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
    private Authentication setAuthentication(Claims claims, HttpServletRequest request, String jwtToken) {
        if (claims.get("username") != null && StringUtils.hasText(claims.get("username").toString())) {
            String username = claims.get("username").toString();
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (userDetails == null || (((CustomUserDetail) userDetails).getEmployee() != null && ((CustomUserDetail) userDetails).getCurrentUserDepartment() == null)) {
                return null;
            } else {
                UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthentication(jwtToken, userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                return authentication;
            }
        } else {
            return null;
        }
    }

}
