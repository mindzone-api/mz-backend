package com.mindzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageResponse implements Serializable {
    private String ownerId;
    private String content;
    private Date sentAt;
}
