package com.branch.chat.models.data;



import com.branch.chat.enums.MessagePriority;
import com.branch.chat.enums.MessageStatus;
import com.branch.chat.enums.UserType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String message;

    private String userName;

    private Long fromUserId;

    private Long toUserId;

    private Date createdAt;

    private Date modifiedAt;

    @Enumerated(EnumType.STRING)
    private UserType messageOwner;

    @Enumerated(EnumType.STRING)
    private MessagePriority messagePriority;

}
