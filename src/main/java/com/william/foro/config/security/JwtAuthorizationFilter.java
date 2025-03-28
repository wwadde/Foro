package com.william.foro.config.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.william.foro.config.Constants.HEADER_AUTHORIZATION;
import static com.william.foro.config.Constants.TOKEN_BEARER_PREFIX;
import static com.william.foro.config.Constants.getSigningKey;
import static com.william.foro.config.Constants.SECRET;

@Component
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {



    private Claims setSigningKey(HttpServletRequest request) {
        String jwt = request.getHeader(HEADER_AUTHORIZATION)
                .replace(TOKEN_BEARER_PREFIX, "");

        return Jwts.parser()
                .verifyWith(getSigningKey(SECRET))
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private void setAuthentication(Claims claims) {
        List<String> authorities = (List<String>) claims.get("authorities");
        Long userId = claims.get("userId", Long.class);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList());

        auth.setDetails(userId);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean isJWTValid(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER_AUTHORIZATION);
        return authenticationHeader != null && authenticationHeader.startsWith(TOKEN_BEARER_PREFIX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (isJWTValid(request, response)) {
                Claims claims = setSigningKey(request);
                if (claims.get("authorities") != null) {
                    setAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

}
