package com.branch.chat.datalayer;

import com.branch.chat.models.data.Message;
import com.branch.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageDataLayer {

    @Autowired
    MessageRepository messageRepository;

    public Message saveMessage(Message m){
        return messageRepository.save(m);
    }

    public List<Message> getAllMessagesOfUser(String userName){
        return messageRepository.getAllMessages(userName);
    }
}
