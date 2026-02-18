package com.githubreader.repository;

import com.githubreader.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {
    Optional<Topic> findByNameAndSubTopic(String name, String subTopic);
}
