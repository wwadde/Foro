package com.william.foro.service;

import com.william.foro.model.dto.UsuarioDTO;
import com.william.foro.config.JwtRequest;

public interface UsuarioService {
    String crearUsuario(UsuarioDTO usuario);
    String autenticarUsuario(JwtRequest request);

}
