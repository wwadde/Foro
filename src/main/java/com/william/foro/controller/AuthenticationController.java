package com.william.foro.controller;

import com.william.foro.model.dto.UsuarioDTO;
import com.william.foro.service.UsuarioService;
import com.william.foro.config.JwtRequest;
import com.william.foro.config.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticar")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final UsuarioService usuarioService;

    @CrossOrigin("*")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> autenticarUsuario(@RequestBody JwtRequest request) {

        log.info("ENTRA EL REQUEST");

        log.info(request.getUsername());
        log.info(request.getPassword());


        String token = usuarioService.autenticarUsuario(request);


        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> crearUsuario(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }
}
