package com.branch.chat.models.output;

import lombok.Data;

import java.util.Set;

@Data
public class ActiveUsersOutput extends ApiOutput{
    private Set<String> activeUsers;
}
