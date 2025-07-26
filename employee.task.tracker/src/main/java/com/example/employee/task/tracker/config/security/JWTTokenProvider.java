package com.example.employee.task.tracker.config.security;


import com.example.employee.task.tracker.model.AuthProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private final String CUR_DEPARTMENT_CLAIM = "departmentCode";


    public String createToken(String username, String role, String departmentCode) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put(CUR_DEPARTMENT_CLAIM, departmentCode);
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String createToken(String username, String role, AuthProvider authProvider) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        String PROVIDER_CLAIM = "provider";
        claims.put(PROVIDER_CLAIM, authProvider.getName());
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token,UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return bearer != null && bearer.startsWith("Bearer ") ? bearer.substring(7) : null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String getCurrentDepartment(HttpServletRequest request) {
        String token = this.resolveToken(request);
        if (token != null) {
            Claims claims = this.getClaims(token);
            if (claims.get(CUR_DEPARTMENT_CLAIM) != null && StringUtils.hasText(claims.get(CUR_DEPARTMENT_CLAIM).toString())) {
                return claims.get(CUR_DEPARTMENT_CLAIM).toString();

            }
        }
        return null;
    }
}
