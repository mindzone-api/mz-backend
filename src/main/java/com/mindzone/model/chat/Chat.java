package com.mindzone.model.chat;

import com.mindzone.model.AbstractModel;
import com.mindzone.model.chat.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "chat")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chat extends AbstractModel implements Serializable {

    private Set<String> usersIds;
    private List<Message> messages;
}
