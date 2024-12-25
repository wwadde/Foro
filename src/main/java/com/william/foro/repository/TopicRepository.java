package com.william.foro.repository;

import com.william.foro.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {

    List<Topic> findByUsuarioId(Long userId);
}
