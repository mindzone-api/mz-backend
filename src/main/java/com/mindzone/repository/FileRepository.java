package com.mindzone.repository;

import com.mindzone.enums.FileType;
import com.mindzone.model.therapy.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {

    List<File> findAllBySessionIdAndFileType(String sessionId, FileType fileType);
}
