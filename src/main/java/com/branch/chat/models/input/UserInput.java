package com.branch.chat.models.input;

import com.branch.chat.enums.UserType;
import lombok.Data;

@Data
public class UserInput {
    private String username;
    private String password;
    private UserType userType;
}
