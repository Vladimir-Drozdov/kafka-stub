package org.example.kafkastub.repository;

import org.example.kafkastub.entity.PostedMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedMessageRepository extends JpaRepository<PostedMessageEntity, Long> {
}