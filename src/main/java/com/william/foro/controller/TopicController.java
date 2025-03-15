package com.william.foro.controller;

import com.william.foro.model.entity.Topic;
import com.william.foro.model.dto.TopicDTO;
import com.william.foro.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicaciones")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody TopicDTO topic) {
        Topic savedTopic = topicService.crearPublicacion(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTopic);
    }

    @GetMapping
    public List<Topic> getAllTopicsFromUser() {
        return topicService.listarPublicaciones();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public Page<Topic> getAllTopics(@RequestParam(defaultValue = "0") int pageNumber,
                                    @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return topicService.listarPublicacionesPageable(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        return topicService.getPublicacionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Secured("ROLE_STAFF")
    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody TopicDTO topicDetails) {
        Topic updatedTopic = topicService.actualizarPublicacion(id, topicDetails);
        return ResponseEntity.ok(updatedTopic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable Long id) {
        if (Boolean.TRUE.equals(topicService.borrarPublicacion(id))){
            return ResponseEntity.ok("Topic deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

}
