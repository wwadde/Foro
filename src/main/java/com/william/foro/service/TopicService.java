package com.william.foro.service;

import com.william.foro.model.entity.Topic;
import com.william.foro.model.dto.TopicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    Topic crearPublicacion(TopicDTO topic);
    List<Topic> listarPublicaciones();
    Optional<Topic> getPublicacionPorId(Long id);
    Topic actualizarPublicacion(Long id, TopicDTO topicDetails);
    Boolean borrarPublicacion(Long id);

    Page<Topic> listarPublicacionesPageable(Pageable pageable);
}
