package com.mindzone.service.impl;

import com.mindzone.dto.response.ChatResponse;
import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.response.listed.ListedChat;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.chat.Chat;
import com.mindzone.model.user.User;
import com.mindzone.repository.ChatRepository;
import com.mindzone.service.interfaces.ChatService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;
import static com.mindzone.exception.ExceptionMessages.CHAT_NOT_FOUND;
import static com.mindzone.exception.ExceptionMessages.INVALID_CHAT;

@AllArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;
    private UserService userService;
    private TherapyService therapyService;
    private UltimateModelMapper m;

    @Override
    public void save(Chat model) {
        model.updateDates();
        chatRepository.save(model);
    }

    @Override
    public Chat getById(String id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(CHAT_NOT_FOUND));
    }

    @Override
    public ChatResponse getChat(User user, String userId) {
        Optional<Chat> chatOp = chatRepository.findByUsersIdsContainingBoth(user.getId(), userId);
        Chat chat;
        if (chatOp.isPresent()) {
            chat = chatOp.get();
        } else {
            User secondUser = userService.getById(userId);
            if (user.getRole() == PATIENT) {
                if (!therapyService.hasActiveTherapy(secondUser, user)) {
                    throw new ApiRequestException(INVALID_CHAT);
                }
            } else if (user.getRole() == PROFESSIONAL) {
                if (!userService.isAlly(user, secondUser)) {
                    throw new ApiRequestException(INVALID_CHAT);
                }
            }

            chat = new Chat(new HashSet<>(), new ArrayList<>());
            chat.getUsersIds().add(user.getId());
            chat.getUsersIds().add(userId);
        }
        save(chat);
        return m.map(chat, ChatResponse.class);
    }

    @Override
    public Page<ListedChat> getHistory(User user, MzPageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
        Page<Chat> chats = chatRepository.findByUsersIdsContaining(user.getId(), pageable);
        return m.pageMap(chats, ListedChat.class);
    }
}
