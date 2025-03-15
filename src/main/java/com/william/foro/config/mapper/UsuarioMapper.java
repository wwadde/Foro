package com.william.foro.config.mapper;

import com.william.foro.model.dto.UsuarioDTO;
import com.william.foro.model.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class UsuarioMapper {
    @Bean
    public Function<UsuarioDTO, Usuario> dtoToUsuarioEntity(ModelMapper mapper) {
        return user -> mapper.map(user, Usuario.class);
    }
}
