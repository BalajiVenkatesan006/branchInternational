package com.branch.chat.controllers;

import com.branch.chat.datalayer.MessageDataLayer;
import com.branch.chat.datalayer.UserDataAccess;
import com.branch.chat.dto.MessageStompModel;
import com.branch.chat.enums.MessagePriority;
import com.branch.chat.enums.UserType;
import com.branch.chat.models.data.BranchUser;
import com.branch.chat.models.data.Message;
import com.branch.chat.models.output.ActiveUsersOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Set;

@RestController
public class BranchMessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserDataAccess userDataAccess;

    @Autowired
    private MessageDataLayer messageDataLayer;

    @MessageMapping("/chat/{from}/{to}")
    public void sendMessage(@DestinationVariable String from,@DestinationVariable String to, MessageStompModel message) {
        System.out.println("handling send message: " + message.getMessageContent() + " to: " + to);
        BranchUser fromUser = userDataAccess.getUser(from);
        BranchUser toUser = userDataAccess.getUser(to);
        if(fromUser == null || toUser == null){
            return;
        }
        else{
            if(fromUser.getUserType() == UserType.CUSTOMER){
                fromUser.setIsQuestioning(true);
                userDataAccess.saveUser(fromUser);
                Set<String> userNames = userDataAccess.getUsersQuestioning();
                ActiveUsersOutput activeUsersOutput = new ActiveUsersOutput();
                activeUsersOutput.setActiveUsers(userNames);
                simpMessagingTemplate.convertAndSend("/topic/activeUsers", userNames);
                if(fromUser.getAssignedAgent() != null){
                    toUser = userDataAccess.getUser(fromUser.getAssignedAgent());
                    to = toUser.getUsername();
                }
            }
            messageDataLayer.saveMessage(constuctMessageRecord(fromUser,toUser, message));
            if(!from.equals(to)){
                simpMessagingTemplate.convertAndSend("/topic/messages/" + from, message);
                simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
            }
            else{
                simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
            }
        }
    }

    private Message constuctMessageRecord(BranchUser fromUser, BranchUser toUser, MessageStompModel message){
        Message m = new Message();
        m.setMessage(message.getMessageContent());
        m.setMessagePriority(MessagePriority.LOW);
        m.setMessageOwner(fromUser.getUserType());
        m.setModifiedAt(new Date());
        m.setCreatedAt(new Date());
        m.setFromUserId(fromUser.getId());
        m.setToUserId(toUser.getId());
        if(fromUser.getUserType() == UserType.CUSTOMER){
            m.setUserName(fromUser.getUsername());
        }
        else{
            m.setUserName(toUser.getUsername());
        }
        return m;
    }
}
