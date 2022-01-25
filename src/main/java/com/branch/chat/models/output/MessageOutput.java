package com.branch.chat.models.output;

import com.branch.chat.models.data.Message;
import lombok.Data;

import java.util.List;

@Data
public class MessageOutput extends ApiOutput{
    private List<Message> messages;
}
