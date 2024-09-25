package com.mindzone.service.impl;

import com.mindzone.dto.request.ChatMessageRequest;
import com.mindzone.dto.response.ChatMessageResponse;
import com.mindzone.dto.response.ChatResponse;
import com.mindzone.dto.request.MzPageRequest;
import com.mindzone.dto.response.listed.ListedChat;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.chat.Chat;
import com.mindzone.model.chat.Message;
import com.mindzone.model.user.User;
import com.mindzone.repository.ChatRepository;
import com.mindzone.service.interfaces.ChatService;
import com.mindzone.service.interfaces.TherapyService;
import com.mindzone.service.interfaces.UserService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mindzone.enums.Role.PATIENT;
import static com.mindzone.enums.Role.PROFESSIONAL;
import static com.mindzone.exception.ExceptionMessages.*;
import static com.mindzone.util.ChatUtil.decrypt;
import static com.mindzone.util.ChatUtil.encrypt;

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
    @Cacheable("chat")
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
    @Cacheable("chat")
    public Page<ListedChat> getHistory(User user, MzPageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
        Page<Chat> chats = chatRepository.findByUsersIdsContaining(user.getId(), pageable);
        return m.pageMap(chats, ListedChat.class);
    }

    @Override
    public ChatMessageResponse sendMessage(User user, String chatId, ChatMessageRequest request) throws Exception {
         Chat chat = getById(chatId);
         if (!chat.getUsersIds().contains(user.getId())) {
             throw new ApiRequestException(USER_UNAUTHORIZED);
         }

         Message message = new Message(user.getId(), encrypt(request.getContent()), new Date());
         chat.getMessages().add(message);
         save(chat);
         message.setContent(request.getContent());
         return m.map(message, ChatMessageResponse.class);
    }

    @Override
    @Cacheable("chat")
    public Page<ChatMessageResponse> getMessageHistory(User user, String chatId, MzPageRequest pageRequest) {
        Chat chat = getById(chatId);
        if (!chat.getUsersIds().contains(user.getId())) {
            throw new ApiRequestException(USER_UNAUTHORIZED);
        }

        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize());

        Page<Message> messages = getPage(chat.getMessages(), pageable);
        messages.getContent().forEach(m -> {
            try {
                m.setContent(decrypt(m.getContent()));
            } catch (Exception e) {
                throw new ApiRequestException(ERROR_IN_MESSAGES_DECRYPT);
            }
        });

        return m.pageMap(messages, ChatMessageResponse.class);
    }



    private static <T> Page<T> getPage(List<T> list, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<T> pageContent;

        if (list.size() < startItem) {
            pageContent = new ArrayList<>(); // Return an empty list if out of bounds
        } else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            pageContent = list.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageContent, pageable, list.size());
    }
}
