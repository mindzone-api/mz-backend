package com.mindzone.service.interfaces;

import com.mindzone.enums.FileType;
import com.mindzone.model.therapy.SessionFile;

import java.util.List;

public interface SessionFileService {
    void save(SessionFile model);

    List<SessionFile> updateSessionFiles(String sessionId, List<SessionFile> newSessionFiles, FileType fileType);

    List<SessionFile> getBySessionIdAndFileType(String sessionId, FileType fileType);
}
