package com.CRUD.App.Service;

import com.CRUD.App.Model.dto.UsuarioDTO;
import com.CRUD.App.config.JwtRequest;

public interface UsuarioService {
    String crearUsuario(UsuarioDTO usuario);
    String autenticarUsuario(JwtRequest jwtRequest, String apiKey);

}
