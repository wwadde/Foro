package com.william.foro.config.security;

import com.william.foro.model.entity.Usuario;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import static com.william.foro.config.Constants.EXPIRATION_TIME;
import static com.william.foro.config.Constants.getSigningKey;
import static com.william.foro.config.Constants.SECRET;
import static com.william.foro.config.Constants.TOKEN_BEARER_PREFIX;

@Configuration
public class JwtAuthenticationConfig {

    public String getJwToken(Usuario usuario){

        String token = Jwts.builder()
                .id("CRUD_JWT")
                .subject(usuario.getUsername())
                .claim("authorities",
                        usuario.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .claim("userId", usuario.getId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(EXPIRATION_TIME)))
                .signWith(getSigningKey(SECRET))
                .compact();

        return TOKEN_BEARER_PREFIX + token;
    }

}
