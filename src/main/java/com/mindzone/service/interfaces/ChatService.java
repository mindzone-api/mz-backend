package com.mindzone.service.interfaces;

import com.mindzone.dto.request.ChatMessageRequest;
import com.mindzone.dto.response.ChatMessageResponse;
import com.mindzone.dto.response.ChatResponse;
import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.response.listed.ListedChat;
import com.mindzone.model.chat.Chat;
import com.mindzone.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatService {

    void save(Chat model);

    Chat getById(String id);

    ChatResponse getChat(User user, String userId);
    Page<ListedChat> getHistory(User user, MzPageRequest pageRequest);

    ChatMessageResponse sendMessage(User user, String chatId, ChatMessageRequest request) throws Exception;

    Page<ChatMessageResponse> getMessageHistory(User user, String chatId, MzPageRequest pageRequest);
}
