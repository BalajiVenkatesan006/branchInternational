package com.branch.chat.models.data;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "messageMapping")
public class MessageMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "messageId")
    private Long messageId;

    @Column(name = "replyId")
    private Long replyId;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "modifiedAt")
    private Date modifiedAt;
}
