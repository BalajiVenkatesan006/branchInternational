package com.branch.chat.models.output;

import com.branch.chat.enums.ErrorCode;
import lombok.Data;

@Data
public class ApiOutput {
    private Boolean success;
    private String message;
    private String errorMessage;
    private ErrorCode errorCode;
}
