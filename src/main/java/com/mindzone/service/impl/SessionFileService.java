package com.mindzone.service.impl;

import com.mindzone.enums.FileType;
import com.mindzone.model.therapy.SessionFile;
import com.mindzone.repository.SessionFileRepository;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SessionFileService implements com.mindzone.service.interfaces.SessionFileService {

    private SessionFileRepository sessionFileRepository;
    private UltimateModelMapper m;

    @Override
    public void save(SessionFile model) {
        model.updateDates();
        sessionFileRepository.save(model);
    }

    @Override
    public List<SessionFile> updateSessionFiles(String sessionId, List<SessionFile> newSessionFiles, FileType fileType) {
        List<SessionFile> sessionFiles = sessionFileRepository.findAllBySessionIdAndFileType(sessionId, fileType);

        // Deleting all files ocurrences that are not on the updated list
        sessionFileRepository.deleteAll(sessionFiles.stream().filter(
                file -> !newSessionFiles.contains(file)
        ).toList());

        for (SessionFile sessionFile : newSessionFiles) {
            if (sessionFiles.contains(sessionFile)) { // comparing using only the id
                SessionFile sessionFileOnList = sessionFiles.get(sessionFiles.indexOf(sessionFile)); // It is possible to retrieve the file because of its id
                m.map(sessionFile, sessionFileOnList);
                sessionFile = sessionFileOnList;
            } else {
                sessionFile.setSessionId(sessionId);
                sessionFile.setFileType(fileType);
                sessionFiles.add(sessionFile);
            }
            save(sessionFile);
        }
        return sessionFiles;
    }

    @Override
    public List<SessionFile> getBySessionIdAndFileType(String sessionId, FileType fileType) {
        return sessionFileRepository.findAllBySessionIdAndFileType(sessionId, fileType);
    }
}
