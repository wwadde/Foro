package com.william.foro.service.impl;

import com.william.foro.model.entity.Topic;
import com.william.foro.model.entity.Usuario;
import com.william.foro.model.dto.TopicDTO;
import com.william.foro.repository.UsuarioRepository;
import com.william.foro.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.william.foro.repository.TopicRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final UsuarioRepository usuarioRepository;


    @Override
    public Topic crearPublicacion(TopicDTO topic){

        Long userId = UsuarioServiceImpl.getCurrentUserById();

        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Usuario not Found by id: " + userId));

        if (topic == null){
            throw new RuntimeException("Topic is null");
        }

        Topic newTopic = new Topic();
        newTopic.setTitle(topic.getTitle());
        newTopic.setMessage(topic.getMessage());
        newTopic.setStatus(Boolean.TRUE);
        newTopic.setCreatedAt(LocalDateTime.now().withNano(0));
        newTopic.setUsuario(usuario);
        return topicRepository.save(newTopic);

    }

    @Override
    public List<Topic> listarPublicaciones(){
        return topicRepository.findByUsuarioId(UsuarioServiceImpl.getCurrentUserById());
    }

    @Override
    public Optional<Topic> getPublicacionPorId(Long id){
        return topicRepository.findById(id);
    }

    @Override
    public Topic actualizarPublicacion(Long id, TopicDTO topicDetails){
        Topic topic = topicRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Topic not Found by id: "+id));

        topic.setMessage(topicDetails.getMessage());
        topic.setTitle(topicDetails.getTitle());
        return topicRepository.save(topic);
    }

    @Override
    public Boolean borrarPublicacion(Long id){
        Topic topic = topicRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Topic not Found by id: " + id));

        topicRepository.delete(topic);
        return true;
    }

    @Override
    public Page<Topic> listarPublicacionesPageable(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }
}
