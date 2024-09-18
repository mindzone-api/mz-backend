package com.mindzone.model.chat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    private String ownerId;
    private String content;
    private Date sentAt;
}
