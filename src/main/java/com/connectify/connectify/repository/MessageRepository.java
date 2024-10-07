package com.connectify.connectify.repository;

import com.connectify.connectify.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findAllByMessengerIdOrderByCreatedAtAsc(String messengerId);
}
