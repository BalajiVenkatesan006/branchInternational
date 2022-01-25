package com.branch.chat.models.data;

import com.branch.chat.enums.UserType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "branchusers")
@Data
public class BranchUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "username",unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "modifiedAt")
    private Date modifiedAt;

    @Column(name = "isOnline")
    private Boolean isOnline;

    @Enumerated(EnumType.STRING)
    @Column(name = "userType")
    private UserType userType;

    @Column(name = "isQuestioning")
    private Boolean isQuestioning;

    @Column(name = "isOccupied")
    private Boolean isOccupied;

    @Column(name = "assignedAgent")
    private String assignedAgent;

}
