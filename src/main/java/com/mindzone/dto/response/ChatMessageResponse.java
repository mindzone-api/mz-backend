package com.mindzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageResponse {
    private String ownerId;
    private String content;
    private Date sentAt;
}
