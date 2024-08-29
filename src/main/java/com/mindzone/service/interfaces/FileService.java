package com.mindzone.service.interfaces;

import com.mindzone.enums.FileType;
import com.mindzone.model.therapy.File;

import java.util.List;

public interface FileService {
    void save(File model);

    List<File> updateSessionFiles(String sessionId, List<File> newFiles, FileType fileType);

    List<File> getBySessionIdAndFileType(String sessionId, FileType fileType);
}
