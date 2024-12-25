package com.william.foro.service.impl;

import com.william.foro.model.entity.Role;
import com.william.foro.model.entity.Usuario;
import com.william.foro.model.dto.UsuarioDTO;
import com.william.foro.repository.RoleRepository;
import com.william.foro.repository.UsuarioRepository;
import com.william.foro.service.UsuarioService;
import com.william.foro.config.JwtRequest;
import com.william.foro.config.security.JwtAuthenticationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtAuthenticationConfig jwtAuthtenticationConfig;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;



    @Override
    public String crearUsuario(UsuarioDTO usuario) {

        Role userRole = roleRepository.findByName("ROLE_USER");

        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("Usuario ya existe");
        }

        Usuario user = new Usuario();
        user.setUsername(usuario.getUsername());
        user.setPassword(passwordEncoder.encode(usuario.getPassword()));
        user.setEmail(usuario.getEmail());
        user.setRoles(Collections.singletonList(userRole));
        usuarioRepository.save(user);
        return "Usuario creado";
    }

    @Override
    public String autenticarUsuario(JwtRequest request) {
        Usuario user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        log.info(user.toString());
        return jwtAuthtenticationConfig.getJwToken(user);
    }


    public static Long getCurrentUserId() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }
}
