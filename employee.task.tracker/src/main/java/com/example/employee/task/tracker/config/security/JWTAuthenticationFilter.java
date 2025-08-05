package com.example.employee.task.tracker.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final Collection<String> excludedPatterns;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JWTAuthenticationFilter(JWTTokenProvider tokenProvider,
                                   CustomUserDetailsService customUserDetailsService,
                                   Collection<String> excludedPatterns) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.excludedPatterns = excludedPatterns;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if (isExcluded(servletPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwtToken = tokenProvider.resolveAccessToken(request);

            if (!StringUtils.hasText(jwtToken) || !tokenProvider.validateToken(jwtToken)) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
                return;
            }
            Claims claims = tokenProvider.getClaims(jwtToken);

            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
            boolean alreadyAuthenticated = existingAuth != null
                    && existingAuth.isAuthenticated()
                    && !(existingAuth instanceof AnonymousAuthenticationToken);

            if (!alreadyAuthenticated) {
                Authentication authentication = buildAuthenticationFromClaims(claims, request, jwtToken);
                if (authentication == null) {
                    SecurityContextHolder.clearContext();
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
                    return;
                } else {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

    } catch(JwtException e) {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}



private Authentication buildAuthenticationFromClaims(Claims claims, HttpServletRequest request, String jwtToken) {
    Object usernameClaim = claims.get("sub");
    if (usernameClaim == null || !StringUtils.hasText(usernameClaim.toString())) {
        return null;
    }

    String username = usernameClaim.toString();
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
    if (userDetails == null) {
        return null;
    }

    if (userDetails instanceof CustomUserDetail) {
        CustomUserDetail custom = (CustomUserDetail) userDetails;
        if (custom.getEmployee() != null && custom.getCurrentUserDepartment() == null) {
            return null;
        }
    }

    UsernamePasswordAuthenticationToken authentication =
            tokenProvider.getAuthentication(jwtToken, userDetails);
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authentication;
}

private boolean isExcluded(String servletPath) {
    if (servletPath == null || excludedPatterns == null || excludedPatterns.isEmpty()) return false;
    return excludedPatterns.stream().anyMatch(pattern -> pathMatcher.match(pattern, servletPath));
}
}
