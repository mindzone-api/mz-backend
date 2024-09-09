package com.mindzone.repository;

import com.mindzone.enums.FileType;
import com.mindzone.model.therapy.SessionFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SessionFileRepository extends MongoRepository<SessionFile, String> {

    List<SessionFile> findAllBySessionIdAndFileType(String sessionId, FileType fileType);
}
