package com.branch.chat.repository;


import com.branch.chat.models.data.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m from Message m where userName = ?1 order by createdAt asc")
    List<Message> getAllMessages(String userName);
}
