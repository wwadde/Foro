package com.william.foro.service.impl;

import com.william.foro.config.errorhandling.exceptions.AuthenticationException;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final JwtAuthenticationConfig jwtAuthtenticationConfig;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final Function<UsuarioDTO, Usuario> dtoToUsuarioEntity;


    @Override
    public String crearUsuario(UsuarioDTO usuario) {

        Role userRole = roleRepository.findByName("ROLE_USER");

        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new AuthenticationException("Usuario ya existe", HttpStatus.BAD_REQUEST);
        }
        String x = switch (usuario.getUsername()){
            case "admin" -> "ROLE_ADMIN";
            case "staff" -> "ROLE_STAFF";
            default -> "ROLE_USER";
        };
        log.info("x {}", x);

        Usuario user = dtoToUsuarioEntity.apply(usuario);
        user.setPassword(passwordEncoder.encode(usuario.getPassword()));
        user.setRoles(Collections.singletonList(userRole));
        usuarioRepository.save(user);
        return "Usuario creado";
    }

    @Override
    public String autenticarUsuario(JwtRequest request) {
        Usuario user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado", HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Contraseña inválida", HttpStatus.BAD_REQUEST);
        }

        log.info(user.toString());
        return jwtAuthtenticationConfig.getJwToken(user);
    }

//    @Override
//    public String autenticarUsuario(JwtRequest request) {
//
//        Usuario usuario = usuarioRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
//        if (Objects.isNull(usuario)) {
//            throw new UsernameNotFoundException("Invalid username or password");
//        }
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            Usuario user = (Usuario) authentication.getPrincipal();
//            log.info(user.toString());
//            return jwtAuthtenticationConfig.getJwToken(user);
//        } catch (AuthenticationException e) {
//            throw new UsernameNotFoundException("Invalid username or password", e);
//        }
//    }


    public static Long getCurrentUserById() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
