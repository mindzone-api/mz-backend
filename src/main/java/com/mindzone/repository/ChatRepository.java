package com.mindzone.repository;


import com.mindzone.model.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query("{ 'usersIds': { $all: [?0, ?1] } }")
    Optional<Chat> findByUsersIdsContainingBoth(String userId1, String userId2);

    Page<Chat> findByUsersIdsContaining(String userId, Pageable pageable);
}
