package com.mindzone.service.impl;

import com.mindzone.enums.FileType;
import com.mindzone.model.therapy.SessionFile;
import com.mindzone.repository.FileRepository;
import com.mindzone.service.interfaces.FileService;
import com.mindzone.util.UltimateModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;
    private UltimateModelMapper m;

    @Override
    public void save(SessionFile model) {
        model.updateDates();
        fileRepository.save(model);
    }

    @Override
    public List<SessionFile> updateSessionFiles(String sessionId, List<SessionFile> newSessionFiles, FileType fileType) {
        List<SessionFile> sessionFiles = fileRepository.findAllBySessionIdAndFileType(sessionId, fileType);

        // Deleting all files ocurrences that are not on the updated list
        fileRepository.deleteAll(sessionFiles.stream().filter(
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
        return fileRepository.findAllBySessionIdAndFileType(sessionId, fileType);
    }
}
