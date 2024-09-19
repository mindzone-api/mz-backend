package com.mindzone.controller;

import com.mindzone.dto.request.ChatMessageRequest;
import com.mindzone.dto.response.ChatMessageResponse;
import com.mindzone.dto.response.ChatResponse;
import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.response.listed.ListedChat;
import com.mindzone.model.user.User;
import com.mindzone.service.interfaces.ChatService;
import com.mindzone.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mindzone.constants.Constants.V1;

@RestController
@AllArgsConstructor
@RequestMapping(V1 + "/chat")
public class ChatController {

    private UserService userService;
    private ChatService chatService;

    @GetMapping("/{userId}")
    public ResponseEntity<ChatResponse> getChat(JwtAuthenticationToken token, @PathVariable String userId) {
        User user = userService.validate(token);
        return ResponseEntity.ok(chatService.getChat(user, userId));
    }

    @GetMapping
    public ResponseEntity<Page<ListedChat>> getHistory(JwtAuthenticationToken token, @RequestBody MzPageRequest pageRequest) {
        User user = userService.validate(token);
        return ResponseEntity.ok(chatService.getHistory(user, pageRequest));
    }

    @PutMapping("/{chatId}")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            JwtAuthenticationToken token,
            @PathVariable String chatId,
            @RequestBody ChatMessageRequest request
    ) throws Exception {
        User user = userService.validate(token);
        return ResponseEntity.ok(chatService.sendMessage(user, chatId, request));
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<Page<ChatMessageResponse>> getMessageHistory(
            JwtAuthenticationToken token,
            @PathVariable String chatId,
            @RequestBody MzPageRequest pageRequest
    ) {
        User user = userService.validate(token);
        return ResponseEntity.ok(chatService.getMessageHistory(user, chatId, pageRequest));
    }
}
