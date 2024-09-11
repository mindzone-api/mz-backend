package com.mindzone.service.impl;

import com.mindzone.enums.FileType;
import com.mindzone.exception.ApiRequestException;
import com.mindzone.model.File;
import com.mindzone.model.ReportFile;
import com.mindzone.model.therapy.SessionFile;
import com.mindzone.repository.SessionFileRepository;
import com.mindzone.util.FileUtil;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mindzone.exception.ExceptionMessages.SESSION_FILE_NOT_FOUND;
import static com.mindzone.util.FileUtil.contains;
import static com.mindzone.util.FileUtil.hasUpdated;

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
    public SessionFile getById(String id) {
        return sessionFileRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(SESSION_FILE_NOT_FOUND));
    }

    @Override
    public List<SessionFile> updateSessionFiles(String sessionId, List<SessionFile> newSessionFiles, FileType fileType) {
        // FIXME
        List<SessionFile> databaseFiles = sessionFileRepository.findAllBySessionIdAndFileType(sessionId, fileType);

        // 1- Delete all files ocurrences that are on mongo and not on the updated list
        for (SessionFile databaseFile : databaseFiles) {

            if  (!contains(newSessionFiles, databaseFile)) {
                sessionFileRepository.delete(databaseFile);
            }
        }

        // 2- Update existing files with new data and update the updatedAt attribute
        for (SessionFile sessionFile : newSessionFiles) {
            if (contains(databaseFiles, sessionFile)) { // comparing using only the id
                SessionFile fileToUpdate = getById(sessionFile.getId());
                if (this.hasUpdated(fileToUpdate, sessionFile)) {
                    fileToUpdate.setName(sessionFile.getName());
                    fileToUpdate.setContent(sessionFile.getContent());
                    fileToUpdate.setIsMedicalRecord(sessionFile.getIsMedicalRecord());
                    save(fileToUpdate);
                    m.map(fileToUpdate, sessionFile);
                }
            } else {
                sessionFile.setSessionId(sessionId);
                sessionFile.setFileType(fileType);
                databaseFiles.add(sessionFile);
                save(sessionFile);
            }
        }
        return newSessionFiles;
    }

    @Override
    public List<SessionFile> getBySessionIdAndFileType(String sessionId, FileType fileType) {
        return sessionFileRepository.findAllBySessionIdAndFileType(sessionId, fileType);
    }

    private boolean hasUpdated(SessionFile oldFile, SessionFile newFile) {
        return FileUtil.hasUpdated(oldFile, newFile) || !oldFile.getIsMedicalRecord().equals(newFile.getIsMedicalRecord());
    }
}
