package com.branch.chat;

import com.branch.chat.datalayer.MessageDataLayer;
import com.branch.chat.datalayer.UserDataAccess;
import com.branch.chat.dto.ResponseMessage;
import com.branch.chat.enums.MessagePriority;
import com.branch.chat.enums.MessageStatus;
import com.branch.chat.enums.UserType;
import com.branch.chat.models.data.BranchUser;
import com.branch.chat.models.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserDataAccess userDataAccess;

    @Autowired
    private MessageDataLayer messageDataLayer;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification() {
        ResponseMessage message = new ResponseMessage("Global Notification");
        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendPrivateNotification(final String userId, final String msg) {
        ResponseMessage message = new ResponseMessage("Private Notification");
        messagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications", message);
    }
}
